package com.rjglez.order.controller.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderRequest (UUID productId, int quantity) {
}
