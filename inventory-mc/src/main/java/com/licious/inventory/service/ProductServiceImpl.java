package com.licious.inventory.service;

import com.licious.inventory.dto.ProductCreateRequest;
import com.licious.inventory.dto.ProductUpdateRequest;
import com.licious.inventory.entity.Product;
import com.licious.inventory.exception.DuplicateProductException;
import com.licious.inventory.exception.ProductNotFoundException;
import com.licious.inventory.model.Category;
import com.licious.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(ProductCreateRequest request) {
        if (productRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateProductException("Product already exists with name: " + request.name());
        }
        Product product = new Product(request.name(), request.category(), request.unit(),
                request.pricePerUnit(), request.initialQuantity(), request.reorderThreshold());
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return findOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> listAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> listByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductUpdateRequest request) {
        Product product = findOrThrow(id);
        product.updateDetails(request.name(), request.pricePerUnit(), request.reorderThreshold());
        return product;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Product restock(Long id, int quantity) {
        Product product = productRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        product.addStock(quantity);
        return product;
    }

    @Override
    @Transactional
    public Product sell(Long id, int quantity) {
        Product product = productRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        product.deductStock(quantity);
        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> lowStockItems() {
        return productRepository.findAll().stream()
                .filter(Product::isLowStock)
                .toList();
    }

    private Product findOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }
}
