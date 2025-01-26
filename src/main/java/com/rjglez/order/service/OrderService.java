package com.rjglez.order.service;

import com.rjglez.order.client.InventoryClient;
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

    private final Map<UUID, OrderEntity> orders;
    private final InventoryClient inventoryClient;

    OrderService(InventoryClient inventoryClient) {
        UUID firstOrderId = UUID.fromString("37800a58-c153-4c9f-a9cd-2f321c151207");
        UUID secondOrderId = UUID.fromString("9c5b5698-32e6-4410-b19e-6b107399b134");
        UUID thirdOrderId = UUID.fromString("2b533830-ff77-4b5e-a8c3-c9c9140fb0aa");
        orders = new HashMap<>();
        orders.put(firstOrderId, OrderEntity.builder()
                .id(firstOrderId)
                .productId(UUID.fromString("8ef0a5c8-10b8-497b-83bb-2d012f6b3d03"))
                .quantity(35)
                .build());
        orders.put(secondOrderId, OrderEntity.builder()
                .id(secondOrderId)
                .productId(UUID.fromString("063ded62-99b7-4323-ab17-ec5933691c7c"))
                .quantity(71)
                .build());
        orders.put(thirdOrderId, OrderEntity.builder()
                .id(thirdOrderId)
                .productId(UUID.fromString("706ba114-a11e-440a-aa28-d2e68a1b7561"))
                .quantity(2)
                .build());
        this.inventoryClient = inventoryClient;
    }
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
