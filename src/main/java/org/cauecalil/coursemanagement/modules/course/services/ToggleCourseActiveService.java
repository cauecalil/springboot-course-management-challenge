package org.cauecalil.coursemanagement.modules.course.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.exceptions.domain.course.CourseNotFoundException;
import org.cauecalil.coursemanagement.exceptions.domain.course.InvalidCourseUpdateException;
import org.cauecalil.coursemanagement.exceptions.domain.professor.ProfessorNotFoundException;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.cauecalil.coursemanagement.modules.professor.repositories.ProfessorRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToggleCourseActiveService {
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;

    public void execute(UUID id, UUID professorId) {
        var professor = professorRepository.findById(professorId)
                .orElseThrow(ProfessorNotFoundException::new);

        var course = courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        if (!course.getProfessor().getId().equals(professor.getId())) {
            throw new InvalidCourseUpdateException("You can only edit your own courses");
        }

        course.setActive(!course.getActive());

        courseRepository.save(course);
    }
}
