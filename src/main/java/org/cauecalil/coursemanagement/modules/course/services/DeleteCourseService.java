package org.cauecalil.coursemanagement.modules.course.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.exceptions.domain.course.CourseNotFoundException;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCourseService {
    private final CourseRepository courseRepository;

    public void execute(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new CourseNotFoundException();
        }

        courseRepository.deleteById(id);
    }
}
