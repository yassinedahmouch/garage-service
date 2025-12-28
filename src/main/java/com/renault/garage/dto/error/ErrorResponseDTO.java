package com.renault.garage.dto.error;

public record ErrorResponseDTO(int status,
                               String error,
                               String errorCode,
                               String message) {}
