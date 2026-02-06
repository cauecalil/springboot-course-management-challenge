package org.cauecalil.coursemanagement.modules.professor.services;

import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.exceptions.domain.professor.ProfessorAlreadyExistsException;
import org.cauecalil.coursemanagement.modules.professor.dtos.CreateProfessorRequestDTO;
import org.cauecalil.coursemanagement.modules.professor.dtos.CreateProfessorResponseDTO;
import org.cauecalil.coursemanagement.modules.professor.entities.ProfessorEntity;
import org.cauecalil.coursemanagement.modules.professor.repositories.ProfessorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProfessorService {
    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateProfessorResponseDTO execute(CreateProfessorRequestDTO request) {
        if (professorRepository.existsByEmail(request.email())) {
            throw new ProfessorAlreadyExistsException();
        }

        var password = passwordEncoder.encode(request.password());

        var professor = ProfessorEntity.builder()
                .name(request.name())
                .email(request.email())
                .password(password)
                .build();

        professor = professorRepository.save(professor);

        return CreateProfessorResponseDTO.builder()
                .id(professor.getId())
                .name(professor.getName())
                .email(professor.getEmail())
                .createdAt(professor.getCreatedAt())
                .updatedAt(professor.getUpdatedAt())
                .build();
    }
}
