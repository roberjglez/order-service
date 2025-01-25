package com.rjglez.order.service;

import com.rjglez.order.controller.request.OrderRequest;
import com.rjglez.order.controller.response.OrderResponse;
import com.rjglez.order.model.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService {

    private final Map<UUID, OrderEntity> orders = new HashMap<>();

    public OrderResponse createOrder(OrderRequest orderRequest) {
        OrderEntity order = OrderEntity.builder()
                .productId(orderRequest.productId())
                .quantity(orderRequest.quantity())
                .build();
        UUID orderId = UUID.randomUUID();
        orders.put(orderId, order);

        return OrderResponse.builder()
                .orderId(orderId)
                .productId(order.productId())
                .quantity(order.quantity())
                .build();
    }

    public OrderResponse getOrder(UUID id) {
        OrderEntity order = orders.getOrDefault(id, null);

        return Objects.isNull(order) ? null :
                OrderResponse.builder()
                        .orderId(id)
                        .productId(order.productId())
                        .quantity(order.quantity())
                        .build();
    }
}
