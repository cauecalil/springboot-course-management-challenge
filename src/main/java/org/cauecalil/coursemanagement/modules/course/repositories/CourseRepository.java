package org.cauecalil.coursemanagement.modules.course.repositories;

import org.cauecalil.coursemanagement.modules.course.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);

    @Query("""
        SELECT c
        FROM CourseEntity c
        WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
            AND (:category IS NULL OR LOWER(c.category) LIKE LOWER(CONCAT('%', :category, '%')))
    """)
    List<CourseEntity> findCourses(String name, String category);
}
