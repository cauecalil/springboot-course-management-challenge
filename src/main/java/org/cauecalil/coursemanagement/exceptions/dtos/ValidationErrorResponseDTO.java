package org.cauecalil.coursemanagement.exceptions.dtos;

import java.util.List;

public record ValidationErrorResponseDTO(
        String error,
        String message,
        List<FieldValidationErrorDTO> details
) {}