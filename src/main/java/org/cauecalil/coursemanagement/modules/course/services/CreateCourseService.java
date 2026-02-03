package org.cauecalil.coursemanagement.modules.course.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.modules.course.dtos.CreateCourseRequestDTO;
import org.cauecalil.coursemanagement.modules.course.dtos.CreateCourseResponseDTO;
import org.cauecalil.coursemanagement.modules.course.entities.CourseEntity;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCourseService {
    private final CourseRepository courseRepository;

    public CreateCourseResponseDTO execute(CreateCourseRequestDTO request) {
        if (courseRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Course already exists");
        }

        CourseEntity course = new CourseEntity();
        course.setName(request.name());
        course.setCategory(request.category());

        CourseEntity savedCourse = courseRepository.save(course);

        return new CreateCourseResponseDTO(
                savedCourse.getId(),
                savedCourse.getName(),
                savedCourse.getCategory(),
                savedCourse.getActive(),
                savedCourse.getCreatedAt(),
                savedCourse.getUpdatedAt()
        );
    }
}
