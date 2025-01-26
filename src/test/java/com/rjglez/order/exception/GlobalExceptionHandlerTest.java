package com.rjglez.order.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    public void handleIllegalArgumentException() {
        // GIVEN
        String errorMessage = "Test error message";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        // WHEN
        ResponseEntity<String> response = exceptionHandler.handleIllegalArgumentException(exception);

        // THEN
        assertThat(HttpStatus.BAD_REQUEST).isEqualTo(response.getStatusCode());
        assertThat(errorMessage).isEqualTo(response.getBody());
    }
}
