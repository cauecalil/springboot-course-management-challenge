package org.cauecalil.coursemanagement.modules.course.dtos;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateCourseRequestDTO(
        @NotBlank
        @Length(min = 5, max = 100)
        String name,

        @NotBlank
        @Length(min = 5, max = 100)
        String category
) {}
