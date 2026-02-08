package org.cauecalil.coursemanagement.modules.professor.services;

import org.cauecalil.coursemanagement.exceptions.domain.professor.InvalidCredentialsException;
import org.cauecalil.coursemanagement.modules.professor.dtos.AuthProfessorRequestDTO;
import org.cauecalil.coursemanagement.modules.professor.entities.ProfessorEntity;
import org.cauecalil.coursemanagement.modules.professor.repositories.ProfessorRepository;
import org.cauecalil.coursemanagement.providers.JWTProvider;
import org.cauecalil.coursemanagement.providers.TokenResultDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthProfessorServiceTest {
    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTProvider jwtProvider;

    @InjectMocks
    private AuthProfessorService authProfessorService;

    @Test
    @DisplayName("Should not be able to authenticate when professor email does not exist")
    void shouldNotBeAbleToAuthenticateWhenProfessorEmailDoesNotExist() {
        var request = AuthProfessorRequestDTO.builder()
                .email("professor@email.com")
                .password("password")
                .build();

        when(professorRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authProfessorService.execute(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(professorRepository).findByEmail(request.email());
        verifyNoMoreInteractions(professorRepository);
        verifyNoInteractions(passwordEncoder, jwtProvider);
    }

    @Test
    @DisplayName("Should not be able to authenticate when password does not match")
    void shouldNotBeAbleToAuthenticateWhenPasswordDoesNotMatch() {
        var request = AuthProfessorRequestDTO.builder()
                .email("professor@email.com")
                .password("wrong-password")
                .build();

        var professor = ProfessorEntity.builder()
                .id(UUID.randomUUID())
                .name("Professor Name")
                .email(request.email())
                .password("encoded-password")
                .build();

        when(professorRepository.findByEmail(request.email())).thenReturn(Optional.of(professor));
        when(passwordEncoder.matches(request.password(), professor.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> authProfessorService.execute(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(professorRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), professor.getPassword());
        verifyNoInteractions(jwtProvider);
        verifyNoMoreInteractions(professorRepository, passwordEncoder);
    }

    @Test
    @DisplayName("Should be able to authenticate professor successfully and return token + expiration")
    void shouldBeAbleToAuthenticateProfessorSuccessfully() {
        var request = AuthProfessorRequestDTO.builder()
                .email("professor@email.com")
                .password("password")
                .build();

        var professorId = UUID.randomUUID();
        var professor = ProfessorEntity.builder()
                .id(professorId)
                .name("Professor Name")
                .email(request.email())
                .password("encoded-password")
                .build();

        var tokenResult = TokenResultDTO.builder()
                .accessToken("jwt-token")
                .expiresAt(123456789L)
                .build();

        when(professorRepository.findByEmail(request.email())).thenReturn(Optional.of(professor));
        when(passwordEncoder.matches(request.password(), professor.getPassword())).thenReturn(true);
        when(jwtProvider.generateToken(professorId.toString(), List.of("PROFESSOR"))).thenReturn(tokenResult);

        var response = authProfessorService.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.access_token()).isEqualTo("jwt-token");
        assertThat(response.expires_at()).isEqualTo(123456789L);

        verify(professorRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), professor.getPassword());
        verify(jwtProvider).generateToken(professorId.toString(), List.of("PROFESSOR"));
        verifyNoMoreInteractions(professorRepository, passwordEncoder, jwtProvider);
    }
}