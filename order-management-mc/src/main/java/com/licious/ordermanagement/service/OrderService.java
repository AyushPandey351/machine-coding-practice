package com.licious.ordermanagement.service;

import com.licious.ordermanagement.dto.OrderCreateRequest;
import com.licious.ordermanagement.entity.Order;
import com.licious.ordermanagement.model.OrderStatus;

import java.util.List;

public interface OrderService {

    Order createOrder(OrderCreateRequest request);

    Order getOrder(Long id);

    List<Order> listAll();

    List<Order> listByStatus(OrderStatus status);

    Order confirmOrder(Long id);

    Order shipOrder(Long id);

    Order deliverOrder(Long id);

    Order cancelOrder(Long id);

    void deleteOrder(Long id);
}
