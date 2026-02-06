package org.cauecalil.coursemanagement.exceptions.dtos;

public record FieldValidationErrorDTO(
        String message,
        String field
) {}

