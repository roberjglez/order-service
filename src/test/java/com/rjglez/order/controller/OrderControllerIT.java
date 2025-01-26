package com.rjglez.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.JsonPath;
import com.rjglez.order.client.InventoryClient;
import com.rjglez.order.controller.request.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIT {

    @MockBean
    private InventoryClient inventoryClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getOrderWhenItExists() throws Exception {
        // GIVEN
        UUID productId = UUID.randomUUID();
        int quantity = 1;
        OrderRequest orderRequest = OrderRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        when(inventoryClient.checkStock(productId)).thenReturn(120);
        doNothing().when(inventoryClient).reduceStock(productId, quantity);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String orderRequestJson = ow.writeValueAsString(orderRequest);

        // WHEN
        MvcResult result = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson))
                .andExpect(status().isOk())
                .andReturn();

        String orderId = JsonPath.read(result.getResponse().getContentAsString(), "$.orderId");

        // THEN
        mockMvc.perform(get("/orders/{id}", orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.productId").value(productId.toString()))
                .andExpect(jsonPath("$.quantity").value(quantity));
    }

    @Test
    void getOrderWhenItDoesNotExist() throws Exception {
        //GIVEN
        String orderId = UUID.randomUUID().toString();

        // THEN
        mockMvc.perform(get("/orders/{id}", orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void createOrder() throws Exception {
        // GIVEN
        UUID productId = UUID.randomUUID();
        int quantity = 1;
        OrderRequest orderRequest = OrderRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        when(inventoryClient.checkStock(productId)).thenReturn(120);
        doNothing().when(inventoryClient).reduceStock(productId, quantity);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String orderRequestJson = ow.writeValueAsString(orderRequest);

        // THEN
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").isNotEmpty())
                .andExpect(jsonPath("$.productId").value(productId.toString()))
                .andExpect(jsonPath("$.quantity").value(quantity));
    }
}
