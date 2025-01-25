package com.rjglez.order.controller.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderResponse(UUID orderId, UUID productId, int quantity) {
}
