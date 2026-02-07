package org.cauecalil.coursemanagement.modules.course.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.exceptions.domain.course.CourseAlreadyExistsException;
import org.cauecalil.coursemanagement.exceptions.domain.course.CourseNotFoundException;
import org.cauecalil.coursemanagement.exceptions.domain.course.InvalidCourseUpdateException;
import org.cauecalil.coursemanagement.exceptions.domain.professor.ProfessorNotFoundException;
import org.cauecalil.coursemanagement.modules.course.dtos.EditCourseRequestDTO;
import org.cauecalil.coursemanagement.modules.course.dtos.EditCourseResponseDTO;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.cauecalil.coursemanagement.modules.professor.repositories.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EditCourseService {
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    public EditCourseResponseDTO execute(UUID id, EditCourseRequestDTO request, UUID professorId) {
        var professor = professorRepository.findById(professorId)
                .orElseThrow(ProfessorNotFoundException::new);

        var course = courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        if (!course.getProfessor().getId().equals(professor.getId())) {
            throw new InvalidCourseUpdateException("You can only edit your own courses");
        }

        boolean hasName = request.name() != null;
        boolean hasCategory = request.category() != null;

        if (hasName == hasCategory) {
            throw new InvalidCourseUpdateException("You must provide exactly one field: name or category");
        }

        if (hasName) {
            if (courseRepository.existsByNameIgnoreCase(request.name())) {
                throw new CourseAlreadyExistsException();
            }

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
