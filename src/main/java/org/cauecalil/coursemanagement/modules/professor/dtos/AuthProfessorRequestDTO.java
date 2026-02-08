package org.cauecalil.coursemanagement.modules.professor.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthProfessorRequestDTO(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password
) {}
