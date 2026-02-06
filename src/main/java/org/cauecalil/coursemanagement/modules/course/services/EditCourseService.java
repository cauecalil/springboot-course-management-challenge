package org.cauecalil.coursemanagement.modules.course.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.modules.course.dtos.EditCourseRequestDTO;
import org.cauecalil.coursemanagement.modules.course.dtos.EditCourseResponseDTO;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EditCourseService {
    private final CourseRepository courseRepository;

    public EditCourseResponseDTO execute(UUID id, EditCourseRequestDTO request) {
        var course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        boolean hasName = request.name() != null;
        boolean hasCategory = request.category() != null;

        if (hasName == hasCategory) {
            throw new IllegalArgumentException("You must provide exactly one field: name or category");
        }

        if (hasName) {
            course.setName(request.name());
        } else {
            course.setCategory(request.category());
        }

        var savedCourse = courseRepository.save(course);

        return EditCourseResponseDTO.builder()
                .id(savedCourse.getId())
                .name(savedCourse.getName())
                .category(savedCourse.getCategory())
                .active(savedCourse.getActive())
                .createdAt(savedCourse.getCreatedAt())
                .updatedAt(savedCourse.getUpdatedAt())
                .build();
    }
}
