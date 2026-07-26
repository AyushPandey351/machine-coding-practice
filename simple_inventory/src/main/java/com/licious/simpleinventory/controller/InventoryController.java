package com.licious.simpleinventory.controller;

import com.licious.simpleinventory.dto.InventoryRequest;
import com.licious.simpleinventory.dto.InventoryResponse;
import com.licious.simpleinventory.service.InventoryService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService service;

    public InventoryController(InventoryService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<InventoryResponse> add(@RequestBody InventoryRequest request) {
        int updatedQuantity = service.addInventory(request.productId(), request.quantity());
        return ResponseEntity.ok(new InventoryResponse(request.productId(), updatedQuantity, "Inventory added successfully"));
    }

    @PostMapping("/deduct")
    public ResponseEntity<InventoryResponse> deduct(@RequestBody InventoryRequest request) {
        int updatedQuantity = service.deductInventory(request.productId(), request.quantity());
        return ResponseEntity.ok(new InventoryResponse(request.productId(), updatedQuantity, "Stock deducted successfully"));
    }

    @GetMapping("/getInventory")
    public ResponseEntity<ConcurrentHashMap<String, AtomicInteger>> getInventory() {
        return ResponseEntity.ok(service.getAllInventory());
    }
}
