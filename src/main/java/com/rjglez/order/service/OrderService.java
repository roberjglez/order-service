package com.rjglez.order.service;

import com.rjglez.order.client.InventoryClient;
import com.rjglez.order.controller.request.OrderRequest;
import com.rjglez.order.controller.response.OrderResponse;
import com.rjglez.order.model.OrderEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderService {

    private final Map<UUID, OrderEntity> orders = new HashMap<>();
    private final InventoryClient inventoryClient;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        UUID productId = orderRequest.productId();
        int quantity = orderRequest.quantity();

        checkStock(productId, quantity);
        inventoryClient.reduceStock(productId, quantity);

        UUID orderId = UUID.randomUUID();
        OrderEntity order = OrderEntity.builder()
                .id(orderId)
                .productId(orderRequest.productId())
                .quantity(orderRequest.quantity())
                .build();
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

    private void checkStock(UUID productId, int quantity) {
        int stock = inventoryClient.checkStock(productId);
        if (stock < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product: " + productId);
        }
    }
}
