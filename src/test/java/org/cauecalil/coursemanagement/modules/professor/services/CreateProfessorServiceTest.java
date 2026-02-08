package org.cauecalil.coursemanagement.modules.professor.services;

import org.cauecalil.coursemanagement.exceptions.domain.professor.ProfessorAlreadyExistsException;
import org.cauecalil.coursemanagement.modules.professor.dtos.CreateProfessorRequestDTO;
import org.cauecalil.coursemanagement.modules.professor.entities.ProfessorEntity;
import org.cauecalil.coursemanagement.modules.professor.repositories.ProfessorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateProfessorServiceTest {
    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateProfessorService createProfessorService;

    @Test
    @DisplayName("Should not be able to create professor with existing email")
    public void shouldNotBeAbleToCreateProfessorWithExistingEmail() {
        var request = CreateProfessorRequestDTO.builder()
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        when(professorRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> createProfessorService.execute(request))
                .isInstanceOf(ProfessorAlreadyExistsException.class);

        verify(professorRepository).existsByEmail(request.email());
        verifyNoMoreInteractions(professorRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Should be able to create professor successfully")
    public void shouldBeAbleToCreateProfessorSuccessfully() {
        var request = CreateProfessorRequestDTO.builder()
                .name("Professor Name")
                .email("professor@email.com")
                .password("password")
                .build();

        var professorId = UUID.randomUUID();
        var encodedPassword = "encoded-password";
        var createdAt = LocalDateTime.now().minusMinutes(1);
        var updatedAt = LocalDateTime.now();

        var savedProfessor = ProfessorEntity.builder()
                .id(professorId)
                .name(request.name())
                .email(request.email())
                .password(encodedPassword)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        when(professorRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(professorRepository.save(any(ProfessorEntity.class))).thenReturn(savedProfessor);

        var response = createProfessorService.execute(request);

        ArgumentCaptor<ProfessorEntity> professorCaptor = ArgumentCaptor.forClass(ProfessorEntity.class);
        verify(professorRepository).save(professorCaptor.capture());

        var entityToSave = professorCaptor.getValue();
        assertThat(entityToSave.getId()).isNull();
        assertThat(entityToSave.getName()).isEqualTo(request.name());
        assertThat(entityToSave.getEmail()).isEqualTo(request.email());
        assertThat(entityToSave.getPassword()).isEqualTo(encodedPassword);

        assertThat(response.id()).isEqualTo(professorId);
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.email()).isEqualTo(request.email());
        assertThat(response.createdAt()).isEqualTo(createdAt);
        assertThat(response.updatedAt()).isEqualTo(updatedAt);

        verify(professorRepository).existsByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verifyNoMoreInteractions(professorRepository, passwordEncoder);
    }
}