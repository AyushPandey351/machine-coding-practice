package com.licious.inventory.controller;

import com.licious.inventory.dto.ProductCreateRequest;
import com.licious.inventory.dto.ProductResponse;
import com.licious.inventory.dto.ProductUpdateRequest;
import com.licious.inventory.dto.StockUpdateRequest;
import com.licious.inventory.entity.Product;
import com.licious.inventory.model.Category;
import com.licious.inventory.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.listAll().stream()
                .map(ProductResponse::from).toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(ProductResponse.from(productService.getProduct(id)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getByCategory(@PathVariable Category category) {
        List<ProductResponse> products = productService.listByCategory(category).stream()
                .map(ProductResponse::from).toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchByName(@RequestParam String name) {
        List<ProductResponse> products = productService.searchByName(name).stream()
                .map(ProductResponse::from).toList();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockItems() {
        List<ProductResponse> products = productService.lowStockItems().stream()
                .map(ProductResponse::from).toList();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                        @Valid @RequestBody ProductUpdateRequest request) {
        Product updated = productService.updateProduct(id, request);
        return ResponseEntity.ok(ProductResponse.from(updated));
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<ProductResponse> restock(@PathVariable Long id,
                                                     @Valid @RequestBody StockUpdateRequest request) {
        Product updated = productService.restock(id, request.quantity());
        return ResponseEntity.ok(ProductResponse.from(updated));
    }

    @PostMapping("/{id}/sell")
    public ResponseEntity<ProductResponse> sell(@PathVariable Long id,
                                                 @Valid @RequestBody StockUpdateRequest request) {
        Product updated = productService.sell(id, request.quantity());
        return ResponseEntity.ok(ProductResponse.from(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
