package org.cauecalil.coursemanagement.exceptions.dtos;

public record ApiErrorResponseDTO(
        String error,
        String message
) {}