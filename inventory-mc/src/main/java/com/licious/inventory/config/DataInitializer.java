package com.licious.inventory.config;

import com.licious.inventory.entity.Product;
import com.licious.inventory.model.Category;
import com.licious.inventory.model.UnitOfMeasure;
import com.licious.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        productRepository.save(new Product("Farm Eggs", Category.EGG, UnitOfMeasure.DOZEN,
                new BigDecimal("89.00"), 100, 20));
        productRepository.save(new Product("Chicken Curry Cut", Category.CHICKEN, UnitOfMeasure.KG,
                new BigDecimal("219.00"), 50, 10));
        productRepository.save(new Product("Chicken Breast Boneless", Category.CHICKEN, UnitOfMeasure.KG,
                new BigDecimal("289.00"), 40, 10));
        productRepository.save(new Product("Mutton Curry Cut", Category.MUTTON, UnitOfMeasure.KG,
                new BigDecimal("699.00"), 15, 10));
        productRepository.save(new Product("Prawns", Category.SEAFOOD, UnitOfMeasure.KG,
                new BigDecimal("499.00"), 30, 5));

        log.info("Seeded {} products. Try: http://localhost:8080/api/products", productRepository.count());
    }
}
