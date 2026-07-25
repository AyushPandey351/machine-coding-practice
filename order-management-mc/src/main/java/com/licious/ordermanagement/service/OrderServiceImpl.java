package com.licious.ordermanagement.service;

import com.licious.ordermanagement.dto.OrderCreateRequest;
import com.licious.ordermanagement.dto.OrderItemRequest;
import com.licious.ordermanagement.entity.Order;
import com.licious.ordermanagement.entity.OrderItem;
import com.licious.ordermanagement.exception.InvalidOrderStateException;
import com.licious.ordermanagement.exception.OrderNotFoundException;
import com.licious.ordermanagement.model.OrderStatus;
import com.licious.ordermanagement.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest request) {
        Order order = new Order(request.customerName());
        for (OrderItemRequest itemRequest : request.items()) {
            order.addItem(new OrderItem(order, itemRequest.productName(),
                    itemRequest.pricePerUnit(), itemRequest.quantity()));
        }
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return findOrThrow(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> listByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    @Transactional
    public Order confirmOrder(Long id) {
        Order order = lockOrThrow(id);
        order.confirm();
        return order;
    }

    @Override
    @Transactional
    public Order shipOrder(Long id) {
        Order order = lockOrThrow(id);
        order.ship();
        return order;
    }

    @Override
    @Transactional
    public Order deliverOrder(Long id) {
        Order order = lockOrThrow(id);
        order.deliver();
        return order;
    }

    @Override
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = lockOrThrow(id);
        order.cancel();
        return order;
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        Order order = lockOrThrow(id);
        if (order.getStatus() != OrderStatus.CANCELLED) {
            throw new InvalidOrderStateException(
                    "Only cancelled orders can be deleted; order " + id + " is " + order.getStatus());
        }
        orderRepository.delete(order);
    }

    private Order findOrThrow(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    /** Pessimistic lock - required for every status-mutating path, see OrderRepository.findByIdForUpdate. */
    private Order lockOrThrow(Long id) {
        return orderRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }
}
