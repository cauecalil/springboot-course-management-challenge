package org.cauecalil.coursemanagement.modules.professor.dtos;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateProfessorResponseDTO(
        UUID id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
