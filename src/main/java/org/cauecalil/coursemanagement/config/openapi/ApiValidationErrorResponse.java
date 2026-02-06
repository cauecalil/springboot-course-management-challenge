package org.cauecalil.coursemanagement.config.openapi;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cauecalil.coursemanagement.exceptions.dtos.ValidationErrorResponseDTO;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Invalid request data - validation failed (VALIDATION_ERROR)",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponseDTO.class))
        )
})
public @interface ApiValidationErrorResponse {}