package org.cauecalil.coursemanagement.modules.professor.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.config.openapi.ApiInternalServerErrorResponse;
import org.cauecalil.coursemanagement.config.openapi.ApiValidationErrorResponse;
import org.cauecalil.coursemanagement.exceptions.dtos.ApiErrorResponseDTO;
import org.cauecalil.coursemanagement.modules.professor.dtos.AuthProfessorRequestDTO;
import org.cauecalil.coursemanagement.modules.professor.dtos.AuthProfessorResponseDTO;
import org.cauecalil.coursemanagement.modules.professor.dtos.CreateProfessorRequestDTO;
import org.cauecalil.coursemanagement.modules.professor.dtos.CreateProfessorResponseDTO;
import org.cauecalil.coursemanagement.modules.professor.services.AuthProfessorService;
import org.cauecalil.coursemanagement.modules.professor.services.CreateProfessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professors")
@RequiredArgsConstructor
@Tag(name = "Professors", description = "Professor authentication and registration endpoints")
public class ProfessorController {
    private final CreateProfessorService createProfessorService;
    private final AuthProfessorService authProfessorService;

    @PostMapping
    @Operation(
            summary = "Register a new professor",
            description = "Creates a new professor account in the system. The password will be encrypted before storage."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Professor registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateProfessorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Professor already exists with this email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
            )
    })
    @ApiValidationErrorResponse
    @ApiInternalServerErrorResponse
    public ResponseEntity<Object> createProfessor(
            @Parameter(description = "Professor registration data", required = true)
            @RequestBody @Valid CreateProfessorRequestDTO request
    ) {
        var result = createProfessorService.execute(request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/auth")
    @Operation(
            summary = "Authenticate a professor",
            description = "Authenticates a professor using email and password, returning a JWT token for accessing protected endpoints"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful - JWT token returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthProfessorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials - email or password incorrect",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
            )
    })
    @ApiValidationErrorResponse
    @ApiInternalServerErrorResponse
    public ResponseEntity<Object> authProfessor(
            @Parameter(description = "Professor authentication credentials", required = true)
            @RequestBody @Valid AuthProfessorRequestDTO request
    ) {
        var result = authProfessorService.execute(request);
        return ResponseEntity.ok().body(result);
    }
}
