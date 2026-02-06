package org.cauecalil.coursemanagement.modules.course.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.modules.course.dtos.FindCoursesQueryDTO;
import org.cauecalil.coursemanagement.modules.course.dtos.FindCoursesResponseDTO;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindCoursesService {
    private final CourseRepository courseRepository;

    public List<FindCoursesResponseDTO> execute(FindCoursesQueryDTO query) {
        var courses = courseRepository.findCourses(query.name(), query.category());

        return courses.stream().map(course ->
                FindCoursesResponseDTO.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .category(course.getCategory())
                        .active(course.getActive())
                        .createdAt(course.getCreatedAt())
                        .updatedAt(course.getUpdatedAt())
                        .build()
        ).toList();
    }
}
