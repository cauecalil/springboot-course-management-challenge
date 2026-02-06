package org.cauecalil.coursemanagement.modules.course.dtos;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record EditCourseResponseDTO(
        UUID id,
        String name,
        String category,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
