package org.cauecalil.coursemanagement.modules.course.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindCoursesResponseDTO(
        UUID id,
        String name,
        String category,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
