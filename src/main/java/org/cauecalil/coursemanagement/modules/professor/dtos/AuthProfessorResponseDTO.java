package org.cauecalil.coursemanagement.modules.professor.dtos;

import lombok.Builder;

@Builder
public record AuthProfessorResponseDTO(
        String access_token,
        Long expires_at
) {}
