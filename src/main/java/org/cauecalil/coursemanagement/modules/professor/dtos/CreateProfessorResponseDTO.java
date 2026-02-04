package org.cauecalil.coursemanagement.modules.professor.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateProfessorResponseDTO(
        UUID id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
