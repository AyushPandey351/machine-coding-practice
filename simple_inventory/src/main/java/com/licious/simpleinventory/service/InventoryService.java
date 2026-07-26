package com.licious.simpleinventory.service;

import com.licious.simpleinventory.exception.InsufficientStockException;
import com.licious.simpleinventory.exception.ProductNotFoundException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final ConcurrentHashMap<String, AtomicInteger> inventory = new ConcurrentHashMap<>();

    public int addInventory(String productId, int quantity) {
        return inventory.computeIfAbsent(productId, k -> new AtomicInteger(0)).addAndGet(quantity);
    }

    public int deductInventory(String productId, int quantity) {
        AtomicInteger stock = inventory.get(productId);
        if (stock == null) {
            throw new ProductNotFoundException("Product not found: " + productId);
        }

        // Retry loop instead of a single compareAndSet: if a concurrent
        // add/deduct changes the value between the read and the CAS, the
        // CAS fails and we re-read + re-check rather than silently no-op'ing
        // (which is the race the original draft had - it ignored the CAS
        // result).
        int currentStock;
        int updatedStock;
        do {
            currentStock = stock.get();
            if (currentStock < quantity) {
                throw new InsufficientStockException("Not enough stock for product: " + productId);
            }
            updatedStock = currentStock - quantity;
        } while (!stock.compareAndSet(currentStock, updatedStock));

        return updatedStock;
    }

    public ConcurrentHashMap<String, AtomicInteger> getAllInventory() {
        return inventory;
    }
}
