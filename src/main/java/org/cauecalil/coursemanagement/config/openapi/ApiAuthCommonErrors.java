package org.cauecalil.coursemanagement.config.openapi;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ApiResponses({
        @ApiResponse(responseCode = "401", description = "User not authenticated", content = @Content),
        @ApiResponse(responseCode = "403", description = "User does not have permission", content = @Content)
})
public @interface ApiAuthCommonErrors {}