package com.licious.ordermanagement.config;

import com.licious.ordermanagement.entity.Order;
import com.licious.ordermanagement.entity.OrderItem;
import com.licious.ordermanagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) {
        Order order1 = new Order("Ravi Kumar");
        order1.addItem(new OrderItem(order1, "Farm Eggs (Dozen)", new BigDecimal("89.00"), 2));
        order1.addItem(new OrderItem(order1, "Chicken Curry Cut (Kg)", new BigDecimal("219.00"), 1));
        orderRepository.save(order1);

        Order order2 = new Order("Priya Sharma");
        order2.addItem(new OrderItem(order2, "Mutton Curry Cut (Kg)", new BigDecimal("699.00"), 1));
        order2.confirm();
        orderRepository.save(order2);

        Order order3 = new Order("Amit Singh");
        order3.addItem(new OrderItem(order3, "Prawns (Kg)", new BigDecimal("499.00"), 2));
        order3.confirm();
        order3.ship();
        orderRepository.save(order3);

        log.info("Seeded {} orders. Try: http://localhost:8081/api/orders", orderRepository.count());
    }
}
