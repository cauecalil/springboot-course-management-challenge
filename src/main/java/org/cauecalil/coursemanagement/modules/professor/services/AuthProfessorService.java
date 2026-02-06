package org.cauecalil.coursemanagement.modules.professor.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.exceptions.domain.professor.InvalidCredentialsException;
import org.cauecalil.coursemanagement.modules.professor.dtos.AuthProfessorRequestDTO;
import org.cauecalil.coursemanagement.modules.professor.dtos.AuthProfessorResponseDTO;
import org.cauecalil.coursemanagement.modules.professor.repositories.ProfessorRepository;
import org.cauecalil.coursemanagement.providers.JWTProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthProfessorService {
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;

    @Value("${security.token.secret}")
    private String secretKey;

    public AuthProfessorResponseDTO execute(AuthProfessorRequestDTO request) {
        var professor = professorRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        var passwordMatches = passwordEncoder.matches(request.password(), professor.getPassword());

        if (!passwordMatches) {
            throw new InvalidCredentialsException();
        }

        var token = jwtProvider.generateToken(
                professor.getId().toString(),
                List.of("PROFESSOR")
        );

        return AuthProfessorResponseDTO.builder()
                .access_token(token.accessToken())
                .expires_at(token.expiresAt())
                .build();
    }
}
