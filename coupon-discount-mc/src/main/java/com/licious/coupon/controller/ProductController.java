package com.licious.coupon.controller;

import com.licious.coupon.dto.ProductResponse;
import com.licious.coupon.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Read-only - lets Postman testing discover product ids without opening the H2 console. */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        List<ProductResponse> products = productRepository.findAll().stream()
                .map(ProductResponse::from).toList();
        return ResponseEntity.ok(products);
    }
}
