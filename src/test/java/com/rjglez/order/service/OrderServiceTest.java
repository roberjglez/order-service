package com.rjglez.order.service;

import com.rjglez.order.client.InventoryClient;
import com.rjglez.order.controller.request.OrderRequest;
import com.rjglez.order.controller.response.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrderWhenOrderExists() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        int quantity = 1;
        OrderRequest orderRequest = OrderRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        when(inventoryClient.checkStock(productId)).thenReturn(55);

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
    void createOrderWhenProductIdDoesNotExist() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        int quantity = 1;
        String errorMessage = "Product " + productId + " not found in inventory";
        OrderRequest orderRequest = OrderRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        doThrow(new IllegalArgumentException(errorMessage)).when(inventoryClient).checkStock(productId);

        // WHEN
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> orderService.createOrder(orderRequest)
        );

        // THEN
        assertThat(exception.getMessage()).isEqualTo(errorMessage);
    }

    @Test
    void createOrderWhenStockIsOk() {
        // GIVEN
        UUID productId = UUID.randomUUID();
        int quantity = 1;
        OrderRequest orderRequest = OrderRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        when(inventoryClient.checkStock(productId)).thenReturn(55);

        // WHEN
        OrderResponse response = orderService.createOrder(orderRequest);

        // THEN
        assertThat(response).isNotNull();
        assertThat(response.orderId()).isNotNull();
        assertThat(response.productId()).isEqualTo(productId);
        assertThat(response.quantity()).isEqualTo(quantity);
    }
}
