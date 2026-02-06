package org.cauecalil.coursemanagement.exceptions.handlers;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.exceptions.api.ApiErrorCode;
import org.cauecalil.coursemanagement.exceptions.api.ApiException;
import org.cauecalil.coursemanagement.exceptions.dtos.ApiErrorResponseDTO;
import org.cauecalil.coursemanagement.exceptions.dtos.FieldValidationErrorDTO;
import org.cauecalil.coursemanagement.exceptions.dtos.ValidationErrorResponseDTO;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<FieldValidationErrorDTO> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(err -> {
            String message = messageSource.getMessage(err, LocaleContextHolder.getLocale());
            errors.add(new FieldValidationErrorDTO(message, err.getField()));
        });

        return ResponseEntity.badRequest().body(
                new ValidationErrorResponseDTO(
                        ApiErrorCode.VALIDATION_ERROR.name(),
                        "Validation failed",
                        errors
                )
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleConstraintViolation(ConstraintViolationException ex) {
        List<FieldValidationErrorDTO> errors = ex.getConstraintViolations().stream()
                .map(v -> new FieldValidationErrorDTO(v.getMessage(), v.getPropertyPath().toString()))
                .toList();

        return ResponseEntity.badRequest().body(
                new ValidationErrorResponseDTO(
                        ApiErrorCode.VALIDATION_ERROR.name(),
                        "Validation failed",
                        errors
                )
        );
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleTransactionSystem(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();

        if (cause instanceof ConstraintViolationException cve) {
            return handleConstraintViolation(cve);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ValidationErrorResponseDTO(
                        ApiErrorCode.INTERNAL_SERVER_ERROR.name(),
                        "Unexpected transaction error",
                        List.of()
                )
        );
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(
                new ApiErrorResponseDTO(ex.getErrorCode().name(), ex.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponseDTO(ApiErrorCode.INTERNAL_SERVER_ERROR.name(), "Unexpected error")
        );
    }
}