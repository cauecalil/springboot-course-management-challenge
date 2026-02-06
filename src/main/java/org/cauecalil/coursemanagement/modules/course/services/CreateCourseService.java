package org.cauecalil.coursemanagement.modules.course.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.modules.course.dtos.CreateCourseRequestDTO;
import org.cauecalil.coursemanagement.modules.course.dtos.CreateCourseResponseDTO;
import org.cauecalil.coursemanagement.modules.course.entities.CourseEntity;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.cauecalil.coursemanagement.modules.professor.repositories.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCourseService {
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    public CreateCourseResponseDTO execute(CreateCourseRequestDTO request, UUID professorId) {
        var professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new IllegalArgumentException("Professor not found"));

        if (courseRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Course already exists");
        }

        CourseEntity course = CourseEntity.builder()
                .name(request.name())
                .category(request.category())
                .professor(professor)
                .build();

        CourseEntity savedCourse = courseRepository.save(course);

        return CreateCourseResponseDTO.builder()
                .id(savedCourse.getId())
                .name(savedCourse.getName())
                .category(savedCourse.getCategory())
                .active(savedCourse.getActive())
                .createdAt(savedCourse.getCreatedAt())
                .updatedAt(savedCourse.getUpdatedAt())
                .build();
    }
}
