package org.cauecalil.coursemanagement.modules.course.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.config.openapi.ApiAuthCommonErrors;
import org.cauecalil.coursemanagement.config.openapi.ApiInternalServerErrorResponse;
import org.cauecalil.coursemanagement.config.openapi.ApiValidationErrorResponse;
import org.cauecalil.coursemanagement.exceptions.dtos.ApiErrorResponseDTO;
import org.cauecalil.coursemanagement.modules.course.dtos.*;
import org.cauecalil.coursemanagement.modules.course.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController {
    private final CreateCourseService createCourseService;
    private final FindCoursesService findCoursesService;
    private final EditCourseService editCourseService;
    private final DeleteCourseService deleteCourseService;
    private final ToggleCourseActiveService toggleCourseActiveService;

    @GetMapping
    @Operation(
            summary = "List all courses",
            description = "Retrieve a list of courses with optional filtering by name and category"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Courses retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = FindCoursesResponseDTO.class)))
            )
    })
    @ApiInternalServerErrorResponse
    public ResponseEntity<List<FindCoursesResponseDTO>> findCourses(
            @Parameter(description = "Query parameters for filtering courses")
            @ModelAttribute FindCoursesQueryDTO query
    ) {
        var result = findCoursesService.execute(query);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('PROFESSOR')")
    @Operation(
            summary = "Create a new course",
            description = "Creates a new course associated with the authenticated professor. The course is created as active by default."
    )
    @SecurityRequirement(name = "jwt_auth")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Course created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCourseResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Professor not found with the provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A course with this name already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
            )
    })
    @ApiValidationErrorResponse
    @ApiAuthCommonErrors
    @ApiInternalServerErrorResponse
    public ResponseEntity<CreateCourseResponseDTO> createCourse(
            @Parameter(description = "Course data to be created", required = true)
            @RequestBody @Valid CreateCourseRequestDTO request,

            @Parameter(hidden = true)
            @AuthenticationPrincipal String userId
    ) {
        UUID professorId = UUID.fromString(userId);
        var result = createCourseService.execute(request, professorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR')")
    @Operation(
            summary = "Update an existing course",
            description = "Updates course information. Only the professor who created the course can edit it."
    )
    @SecurityRequirement(name = "jwt_auth")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Course updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditCourseResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found with the provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A course with this name already exists",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
            )
    })
    @ApiValidationErrorResponse
    @ApiAuthCommonErrors
    @ApiInternalServerErrorResponse
    public ResponseEntity<EditCourseResponseDTO> editCourse(
            @Parameter(description = "Course ID", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,

            @Parameter(description = "Updated course data", required = true)
            @RequestBody @Valid EditCourseRequestDTO request,

            @Parameter(hidden = true)
            @AuthenticationPrincipal String userId
    ) {
        UUID professorId = UUID.fromString(userId);
        var result = editCourseService.execute(id, request, professorId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PROFESSOR')")
    @Operation(
            summary = "Delete a course",
            description = "Permanently deletes a course. Only the professor who created the course can delete it."
    )
    @SecurityRequirement(name = "jwt_auth")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Course deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found with the provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
            ),
    })
    @ApiValidationErrorResponse
    @ApiAuthCommonErrors
    @ApiInternalServerErrorResponse
    public ResponseEntity<Void> deleteCourse(
            @Parameter(description = "Course ID to delete", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id
    ) {
        deleteCourseService.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/active")
    @PreAuthorize("hasAnyRole('PROFESSOR')")
    @Operation(
            summary = "Toggle course active status",
            description = "Activates or deactivates a course. Only the professor who created the course can toggle its status."
    )
    @SecurityRequirement(name = "jwt_auth")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Course status toggled successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Course not found with the provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponseDTO.class))
            )
    })
    @ApiValidationErrorResponse
    @ApiAuthCommonErrors
    @ApiInternalServerErrorResponse
    public ResponseEntity<Void> toggleCourseActive(
            @Parameter(description = "Course ID to toggle status", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,

            @Parameter(hidden = true)
            @AuthenticationPrincipal String userId
    ) {
        UUID professorId = UUID.fromString(userId);
        toggleCourseActiveService.execute(id, professorId);
        return ResponseEntity.noContent().build();
    }
}
