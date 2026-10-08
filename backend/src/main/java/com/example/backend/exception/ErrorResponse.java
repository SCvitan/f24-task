package com.example.backend.exception;

public record ErrorResponse(
        int status,
        String message
) {
}
