package org.cauecalil.coursemanagement.modules.course.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.modules.course.entities.CourseEntity;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ToggleCourseActiveService {
    private final CourseRepository courseRepository;

    public CourseEntity execute(UUID id) {
        var course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        course.setActive(!course.getActive());

        return courseRepository.save(course);
    }
}
