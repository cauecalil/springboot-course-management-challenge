package org.cauecalil.coursemanagement.modules.course.services;

import org.cauecalil.coursemanagement.modules.course.dtos.FindCoursesQueryDTO;
import org.cauecalil.coursemanagement.modules.course.entities.CourseEntity;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindCoursesServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private FindCoursesService findCoursesService;

    @Test
    @DisplayName("Should return empty list when repository returns no courses")
    void shouldReturnEmptyListWhenRepositoryReturnsNoCourses() {
        var query = FindCoursesQueryDTO.builder()
                .name(null)
                .category(null)
                .build();

        when(courseRepository.findCourses(null, null)).thenReturn(List.of());

        var result = findCoursesService.execute(query);

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(courseRepository).findCourses(null, null);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    @DisplayName("Should call repository with query params and map entity to DTO correctly")
    void shouldCallRepositoryWithQueryParamsAndMapEntityToDtoCorrectly() {
        var query = FindCoursesQueryDTO.builder()
                .name("java")
                .category("backend")
                .build();

        var courseId = UUID.randomUUID();
        var createdAt = LocalDateTime.now().minusDays(2);
        var updatedAt = LocalDateTime.now().minusDays(1);

        var course = CourseEntity.builder()
                .id(courseId)
                .name("Java for Beginners")
                .category("Backend")
                .active(true)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        when(courseRepository.findCourses("java", "backend")).thenReturn(List.of(course));

        var result = findCoursesService.execute(query);

        assertThat(result).hasSize(1);
        var dto = result.getFirst();

        assertThat(dto.id()).isEqualTo(courseId);
        assertThat(dto.name()).isEqualTo("Java for Beginners");
        assertThat(dto.category()).isEqualTo("Backend");
        assertThat(dto.active()).isTrue();
        assertThat(dto.createdAt()).isEqualTo(createdAt);
        assertThat(dto.updatedAt()).isEqualTo(updatedAt);

        verify(courseRepository).findCourses("java", "backend");
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    @DisplayName("Should pass null filters to repository when query has null name/category")
    void shouldPassNullFiltersToRepositoryWhenQueryHasNullNameCategory() {
        var query = FindCoursesQueryDTO.builder()
                .name(null)
                .category(null)
                .build();

        when(courseRepository.findCourses(null, null)).thenReturn(List.of());

        findCoursesService.execute(query);

        verify(courseRepository).findCourses(null, null);
        verifyNoMoreInteractions(courseRepository);
    }
}