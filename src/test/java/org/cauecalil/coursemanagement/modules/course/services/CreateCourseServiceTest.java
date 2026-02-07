package org.cauecalil.coursemanagement.modules.course.services;

import org.cauecalil.coursemanagement.exceptions.domain.course.CourseAlreadyExistsException;
import org.cauecalil.coursemanagement.exceptions.domain.professor.ProfessorNotFoundException;
import org.cauecalil.coursemanagement.modules.course.dtos.CreateCourseRequestDTO;
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
public class CreateCourseServiceTest {
    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CreateCourseService createCourseService;

    @Test
    @DisplayName("Should not be able to create course without professor")
    public void shouldNotBeAbleToCreateCourseWithoutProfessor() {
        var professorId = UUID.randomUUID();

        var request = CreateCourseRequestDTO.builder()
                .name("Course Name")
                .category("Course Category")
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createCourseService.execute(request, professorId))
                .isInstanceOf(ProfessorNotFoundException.class);

        verify(professorRepository).findById(professorId);
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should not be able to create course when a course with same name already exists")
    public void shouldNotBeAbleToCreateCourseWhenCourseAlreadyExists() {
        var professorId = UUID.randomUUID();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var request = CreateCourseRequestDTO.builder()
                .name("Course Name")
                .category("Course Category")
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.existsByNameIgnoreCase(request.name())).thenReturn(true);

        assertThatThrownBy(() -> createCourseService.execute(request, professorId))
                .isInstanceOf(CourseAlreadyExistsException.class);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).existsByNameIgnoreCase(request.name());
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should be able to create course successfully")
    public void shouldBeAbleToCreateCourseSuccessfully() {
        var professorId = UUID.randomUUID();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var request = CreateCourseRequestDTO.builder()
                .name("Course Name")
                .category("Course Category")
                .build();

        var courseId = UUID.randomUUID();
        var createdAt = LocalDateTime.now().minusMinutes(1);
        var updatedAt = LocalDateTime.now();

        var savedCourse = CourseEntity.builder()
                .id(courseId)
                .name(request.name())
                .category(request.category())
                .active(true)
                .professor(professor)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.existsByNameIgnoreCase(request.name())).thenReturn(false);
        when(courseRepository.save(any(CourseEntity.class))).thenReturn(savedCourse);

        var response = createCourseService.execute(request, professorId);

        ArgumentCaptor<CourseEntity> courseCaptor = ArgumentCaptor.forClass(CourseEntity.class);
        verify(courseRepository).save(courseCaptor.capture());

        var entityToSave = courseCaptor.getValue();
        assertThat(entityToSave.getId()).isNull();
        assertThat(entityToSave.getName()).isEqualTo(request.name());
        assertThat(entityToSave.getCategory()).isEqualTo(request.category());
        assertThat(entityToSave.getActive()).isTrue();
        assertThat(entityToSave.getProfessor()).isEqualTo(professor);

        assertThat(response.id()).isEqualTo(courseId);
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.category()).isEqualTo(request.category());
        assertThat(response.active()).isTrue();
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).existsByNameIgnoreCase(request.name());
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }
}