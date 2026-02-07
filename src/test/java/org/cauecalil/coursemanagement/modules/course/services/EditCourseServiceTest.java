package org.cauecalil.coursemanagement.modules.course.services;

import org.cauecalil.coursemanagement.exceptions.domain.course.CourseAlreadyExistsException;
import org.cauecalil.coursemanagement.exceptions.domain.course.CourseNotFoundException;
import org.cauecalil.coursemanagement.exceptions.domain.course.InvalidCourseUpdateException;
import org.cauecalil.coursemanagement.exceptions.domain.professor.ProfessorNotFoundException;
import org.cauecalil.coursemanagement.modules.course.dtos.EditCourseRequestDTO;
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
public class EditCourseServiceTest {
    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EditCourseService editCourseService;

    @Test
    @DisplayName("Should not be able to edit course without professor")
    public void shouldNotBeAbleToEditCourseWithoutProfessor() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var request = EditCourseRequestDTO.builder()
                .name("Course Name")
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> editCourseService.execute(courseId, request, professorId))
                .isInstanceOf(ProfessorNotFoundException.class);

        verify(professorRepository).findById(professorId);
        verifyNoMoreInteractions(professorRepository);
        verifyNoInteractions(courseRepository);
    }

    @Test
    @DisplayName("Should not be able to edit course without course")
    public void shouldNotBeAbleToEditCourseWithoutCourse() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var request = EditCourseRequestDTO.builder()
                .name("Course Name")
                .build();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> editCourseService.execute(courseId, request, professorId))
                .isInstanceOf(CourseNotFoundException.class);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).save(any(CourseEntity.class));
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should not be able to edit another professor course")
    public void shouldNotBeAbleToEditAnotherProfessorCourse() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var request = EditCourseRequestDTO.builder()
                .name("Course Name")
                .build();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var anotherProfessorId = UUID.randomUUID();

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

        assertThatThrownBy(() -> editCourseService.execute(courseId, request, professorId))
                .isInstanceOf(InvalidCourseUpdateException.class);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).existsByNameIgnoreCase(anyString());
        verify(courseRepository, never()).save(any(CourseEntity.class));
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should not be able to edit course when request has no fields")
    public void shouldNotBeAbleToEditCourseWhenRequestHasNoFields() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var request = EditCourseRequestDTO.builder().build();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var courseProfessor = ProfessorEntity.builder()
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
                .professor(courseProfessor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        assertThatThrownBy(() -> editCourseService.execute(courseId, request, professorId))
                .isInstanceOf(InvalidCourseUpdateException.class);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).existsByNameIgnoreCase(anyString());
        verify(courseRepository, never()).save(any(CourseEntity.class));
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should not be able to edit course name and category at the same time")
    public void shouldNotBeAbleToEditCourseNameAndCategoryAtTheSameTime() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var request = EditCourseRequestDTO.builder()
                .name("Course Name")
                .category("Course Category")
                .build();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var courseProfessor = ProfessorEntity.builder()
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
                .professor(courseProfessor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        assertThatThrownBy(() -> editCourseService.execute(courseId, request, professorId))
                .isInstanceOf(InvalidCourseUpdateException.class);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).existsByNameIgnoreCase(anyString());
        verify(courseRepository, never()).save(any(CourseEntity.class));
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should not be able to edit course with same name of another course")
    public void shouldNotBeAbleToEditCourseWithSameNameOfAnotherCourse() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var request = EditCourseRequestDTO.builder()
                .name("Another Course Name")
                .build();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var courseProfessor = ProfessorEntity.builder()
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
                .professor(courseProfessor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.existsByNameIgnoreCase("Another Course Name")).thenReturn(true);

        assertThatThrownBy(() -> editCourseService.execute(courseId, request, professorId))
                .isInstanceOf(CourseAlreadyExistsException.class);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository).existsByNameIgnoreCase("Another Course Name");
        verify(courseRepository, never()).save(any(CourseEntity.class));
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should be able to edit course name successfully")
    public void shouldBeAbleToEditCourseNameSuccessfully() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var request = EditCourseRequestDTO.builder()
                .name("New Course Name")
                .build();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        // Different instance (same id) to ensure ownership check doesn't rely on reference equality
        var courseProfessor = ProfessorEntity.builder()
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
                .professor(courseProfessor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        var savedCourse = CourseEntity.builder()
                .id(courseId)
                .name("New Course Name")
                .category(course.getCategory())
                .active(course.getActive())
                .professor(course.getProfessor())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt().plusMinutes(1))
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.existsByNameIgnoreCase("New Course Name")).thenReturn(false);
        when(courseRepository.save(any(CourseEntity.class))).thenReturn(savedCourse);

        var response = editCourseService.execute(courseId, request, professorId);

        ArgumentCaptor<CourseEntity> courseCaptor = ArgumentCaptor.forClass(CourseEntity.class);
        verify(courseRepository).save(courseCaptor.capture());

        var entityToSave = courseCaptor.getValue();
        assertThat(entityToSave.getId()).isEqualTo(courseId);
        assertThat(entityToSave.getName()).isEqualTo(request.name());
        assertThat(entityToSave.getCategory()).isEqualTo("Course Category");
        assertThat(entityToSave.getActive()).isEqualTo(course.getActive());
        assertThat(entityToSave.getProfessor()).isEqualTo(courseProfessor);

        assertThat(response.id()).isEqualTo(courseId);
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.category()).isEqualTo("Course Category");
        assertThat(response.active()).isTrue();

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository).existsByNameIgnoreCase("New Course Name");
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }

    @Test
    @DisplayName("Should be able to edit course category successfully")
    public void shouldBeAbleToEditCourseCategorySuccessfully() {
        var courseId = UUID.randomUUID();
        var professorId = UUID.randomUUID();

        var request = EditCourseRequestDTO.builder()
                .category("New Course Category")
                .build();

        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        // Different instance (same id) to ensure ownership check doesn't rely on reference equality
        var courseProfessor = ProfessorEntity.builder()
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
                .professor(courseProfessor)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        var savedCourse = CourseEntity.builder()
                .id(courseId)
                .name(course.getName())
                .category("New Course Category")
                .active(course.getActive())
                .professor(course.getProfessor())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt().plusMinutes(1))
                .build();

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professor));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(CourseEntity.class))).thenReturn(savedCourse);

        var response = editCourseService.execute(courseId, request, professorId);

        ArgumentCaptor<CourseEntity> courseCaptor = ArgumentCaptor.forClass(CourseEntity.class);
        verify(courseRepository).save(courseCaptor.capture());

        var entityToSave = courseCaptor.getValue();
        assertThat(entityToSave.getId()).isEqualTo(courseId);
        assertThat(entityToSave.getName()).isEqualTo("Course Name");
        assertThat(entityToSave.getCategory()).isEqualTo(request.category());
        assertThat(entityToSave.getActive()).isEqualTo(course.getActive());
        assertThat(entityToSave.getProfessor()).isEqualTo(courseProfessor);

        assertThat(response.id()).isEqualTo(courseId);
        assertThat(response.name()).isEqualTo("Course Name");
        assertThat(response.category()).isEqualTo(request.category());
        assertThat(response.active()).isTrue();

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findById(courseId);
        verify(courseRepository, never()).existsByNameIgnoreCase(anyString());
        verifyNoMoreInteractions(professorRepository, courseRepository);
    }
}
