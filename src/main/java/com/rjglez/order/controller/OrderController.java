package com.rjglez.order.controller;

import com.rjglez.order.controller.request.OrderRequest;
import com.rjglez.order.controller.response.OrderResponse;
import com.rjglez.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Order Service", description = "Endpoints for managing orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(description = "Creates a new order for a given product and quantity. Checks inventory before processing")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @Operation(description = "Returns the information for a given order ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderResponse> getOrder(@PathVariable(value = "id") UUID id) {
        OrderResponse order = orderService.getOrder(id);
        return Objects.isNull(order) ? ResponseEntity.noContent().build() : ResponseEntity.ok(order);
    }

}
