package com.licious.inventory.service;

import com.licious.inventory.dto.ProductCreateRequest;
import com.licious.inventory.dto.ProductUpdateRequest;
import com.licious.inventory.entity.Product;
import com.licious.inventory.model.Category;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductCreateRequest request);

    Product getProduct(Long id);

    List<Product> listAll();

    List<Product> listByCategory(Category category);

    List<Product> searchByName(String name);

    Product updateProduct(Long id, ProductUpdateRequest request);

    void deleteProduct(Long id);

    Product restock(Long id, int quantity);

    Product sell(Long id, int quantity);

    List<Product> lowStockItems();
}
