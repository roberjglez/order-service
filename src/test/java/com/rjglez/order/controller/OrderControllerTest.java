package com.rjglez.order.controller;

import com.rjglez.order.controller.request.OrderRequest;
import com.rjglez.order.controller.response.OrderResponse;
import com.rjglez.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrderWhenOrderExists() {
        // GIVEN
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        int quantity = 1;

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(orderId)
                .productId(productId)
                .quantity(quantity)
                .build();

        when(orderService.getOrder(orderId)).thenReturn(orderResponse);

        // WHEN
        ResponseEntity<OrderResponse> response = orderController.getOrder(orderId);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(orderResponse);
        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void getOrderWhenOrderDoesNotExist() {
        // WHEN
        UUID orderId = UUID.randomUUID();
        ResponseEntity<OrderResponse> response = orderController.getOrder(orderId);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        verify(orderService, times(1)).getOrder(orderId);
    }

    @Test
    void createOrder() {
        // GIVEN
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        int quantity = 1;
        OrderRequest orderRequest = OrderRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(orderId)
                .productId(productId)
                .quantity(quantity)
                .build();

        when(orderService.createOrder(orderRequest)).thenReturn(orderResponse);

        // WHEN
        ResponseEntity<OrderResponse> response = orderController.createOrder(orderRequest);

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isEqualTo(orderResponse);
        verify(orderService, times(1)).createOrder(orderRequest);
    }
}
