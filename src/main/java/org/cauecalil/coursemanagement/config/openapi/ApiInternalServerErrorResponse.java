package org.cauecalil.coursemanagement.config.openapi;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.cauecalil.coursemanagement.exceptions.dtos.ApiErrorResponseDTO;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponses({
        @ApiResponse(
                responseCode = "500",
                description = "Unexpected error (INTERNAL_SERVER_ERROR)",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
        )
})
public @interface ApiInternalServerErrorResponse {}