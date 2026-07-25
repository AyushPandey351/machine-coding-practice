package com.licious.coupon.config;

import com.licious.coupon.entity.Coupon;
import com.licious.coupon.entity.Product;
import com.licious.coupon.entity.User;
import com.licious.coupon.model.Category;
import com.licious.coupon.model.DiscountType;
import com.licious.coupon.repository.CouponRepository;
import com.licious.coupon.repository.ProductRepository;
import com.licious.coupon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    @Override
    public void run(String... args) {
        Product eggs = productRepository.save(new Product("Farm Eggs (Dozen)", Category.EGG, new BigDecimal("89.00")));
        Product chicken = productRepository.save(new Product("Chicken Curry Cut (Kg)", Category.CHICKEN, new BigDecimal("219.00")));
        productRepository.save(new Product("Mutton Curry Cut (Kg)", Category.MUTTON, new BigDecimal("699.00")));
        productRepository.save(new Product("Prawns (Kg)", Category.SEAFOOD, new BigDecimal("499.00")));
        productRepository.save(new Product("Fish Curry Cut (Kg)", Category.FISH, new BigDecimal("349.00")));

        userRepository.save(new User("Ravi Kumar", "ravi@example.com"));
        userRepository.save(new User("Priya Sharma", "priya@example.com"));
        userRepository.save(new User("Amit Singh", "amit@example.com"));

        Instant thirtyDaysOut = Instant.now().plus(30, ChronoUnit.DAYS);

        couponRepository.save(new Coupon("WELCOME50", DiscountType.FLAT, new BigDecimal("50"),
                new BigDecimal("200"), null, Set.of(), 1, 100, thirtyDaysOut));

        couponRepository.save(new Coupon("CHICKEN20", DiscountType.PERCENTAGE, new BigDecimal("20"),
                new BigDecimal("100"), new BigDecimal("100"), Set.of(chicken), 2, null, thirtyDaysOut));

        couponRepository.save(new Coupon("LIMITED5", DiscountType.FLAT, new BigDecimal("30"),
                new BigDecimal("50"), null, Set.of(), 1, 5, thirtyDaysOut));

        log.info("Seeded {} products, {} users, {} coupons. Try: http://localhost:8082/api/coupons",
                productRepository.count(), userRepository.count(), couponRepository.count());
    }
}
