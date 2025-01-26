package com.rjglez.order.client;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class InventoryClient {

    private final RestTemplate restTemplate;

    public int checkStock(UUID productId) {
        String inventoryUrl = "http://localhost:8082/inventory/check/" + productId.toString();
        ResponseEntity<InventoryResponse> inventoryResponse = restTemplate.getForEntity(inventoryUrl, InventoryResponse.class);

        checkProductExists(inventoryResponse, productId);

        return Objects.isNull(inventoryResponse.getBody()) ? 0
                : inventoryResponse.getBody().quantity();
    }

    public void reduceStock(UUID productId, int quantity) {
        String reduceStockUrl = "http://localhost:8082/inventory/reduce/{productId}?quantity={quantity}";
        ResponseEntity<InventoryResponse> inventoryResponse = restTemplate.exchange(reduceStockUrl, HttpMethod.PUT, null, InventoryResponse.class, productId.toString(), quantity);

        checkProductExists(inventoryResponse, productId);
        if (inventoryResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new IllegalArgumentException("Insufficient stock for product: " + productId);
        }
    }

    private void checkProductExists(ResponseEntity<InventoryResponse> inventoryResponse, UUID productId) {
        if (inventoryResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new IllegalArgumentException("Product " + productId + " not found in inventory");
        }
    }
}
