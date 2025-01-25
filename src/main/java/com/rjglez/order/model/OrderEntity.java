package com.rjglez.order.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderEntity (UUID productId, int quantity) {
}
