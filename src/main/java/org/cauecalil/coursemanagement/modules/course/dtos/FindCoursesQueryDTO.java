package org.cauecalil.coursemanagement.modules.course.dtos;

import lombok.Builder;

@Builder
public record FindCoursesQueryDTO(
      String name,
      String category
) {}
