package org.cauecalil.coursemanagement.modules.course.services;

import org.cauecalil.coursemanagement.exceptions.domain.course.CourseNotFoundException;
import org.cauecalil.coursemanagement.exceptions.domain.course.InvalidCourseUpdateException;
import org.cauecalil.coursemanagement.exceptions.domain.professor.ProfessorNotFoundException;
import org.cauecalil.coursemanagement.modules.course.entities.CourseEntity;
import org.cauecalil.coursemanagement.modules.course.repositories.CourseRepository;
import org.cauecalil.coursemanagement.modules.professor.entities.ProfessorEntity;
import org.cauecalil.coursemanagement.modules.professor.repositories.ProfessorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCourseServiceTest {
    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private DeleteCourseService deleteCourseService;

    @Test
    @DisplayName("Should not be able to delete course without professor")
    public void shouldNotBeAbleToDeleteCourseWithoutProfessor() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteCourseService.execute(courseId, professorId))
                .isInstanceOf(ProfessorNotFoundException.class);

        verify(professorRepository).findById(professorId);
        verifyNoMoreInteractions(professorRepository);
        verifyNoInteractions(courseRepository);
    }

    @Test
    @DisplayName("Should not be able to delete course when course does not exist")
    public void shouldNotBeAbleToDeleteWhenCourseDoesNotExist() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteCourseService.execute(courseId, professorId))
                .isInstanceOf(CourseNotFoundException.class);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).deleteById(any(UUID.class));
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should not be able to delete another professor course")
    public void shouldNotBeAbleToDeleteAnotherProfessorCourse() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();
        var anotherProfessorId = UUID.randomUUID();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var anotherProfessor = ProfessorEntity.builder()
                .id(anotherProfessorId)
                .name("Another Professor Name")
                .email("another.professor@email.com")
                .password("password")
                .build();

        var course = CourseEntity.builder()
                .id(courseId)
                .name("Course Name")
                .category("Course Category")
                .active(true)
                .professor(anotherProfessor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        assertThatThrownBy(() -> deleteCourseService.execute(courseId, professorId))
                .isInstanceOf(InvalidCourseUpdateException.class);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).deleteById(any(UUID.class));
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should be able to delete course successfully")
    public void shouldBeAbleToDeleteCourseSuccessfully() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var course = CourseEntity.builder()
                .id(courseId)
                .name("Course Name")
                .category("Course Category")
                .active(true)
                .professor(professor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        deleteCourseService.execute(courseId, professorId);

        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(courseRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(courseId);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }
}
