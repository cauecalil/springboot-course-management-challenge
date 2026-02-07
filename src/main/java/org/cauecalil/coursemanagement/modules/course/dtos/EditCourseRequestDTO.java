package org.cauecalil.coursemanagement.modules.course.dtos;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record EditCourseRequestDTO(
        @Size(min = 5, max = 100)
        String name,

        @Size(min = 5, max = 100)
        String category
) {}
