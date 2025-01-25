package com.rjglez.order.service;

import com.rjglez.order.controller.request.OrderRequest;
import com.rjglez.order.controller.response.OrderResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderServiceTest {

    private final OrderService orderService = new OrderService();

    @Test
    void getOrderWhenOrderExists() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        int quantity = 1;
        OrderRequest orderRequest = OrderRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        OrderResponse createResponse = orderService.createOrder(orderRequest);
        UUID orderId = createResponse.orderId();

        // WHEN
        OrderResponse response = orderService.getOrder(orderId);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.orderId()).isEqualTo(orderId);
        assertThat(response.productId()).isEqualTo(productId);
        assertThat(response.quantity()).isEqualTo(quantity);
    }

    @Test
    void getOrderWhenOrderDoesNotExist() {
        // WHEN
        OrderResponse response = orderService.getOrder(UUID.randomUUID());

        // THEN
        assertThat(response).isNull();
    }

    @Test
    void createOrder() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        int quantity = 1;
        OrderRequest orderRequest = OrderRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        // WHEN
        OrderResponse response = orderService.createOrder(orderRequest);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.orderId()).isNotNull();
        assertThat(response.productId()).isEqualTo(productId);
        assertThat(response.quantity()).isEqualTo(quantity);
    }
}
