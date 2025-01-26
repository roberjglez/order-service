package com.rjglez.order.client;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InventoryResponse(UUID productId, int quantity) {
}
