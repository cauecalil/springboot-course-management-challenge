package org.cauecalil.coursemanagement.modules.professor.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateProfessorRequestDTO(
        @NotBlank
        @Length(min = 5, max = 100)
        String name,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Length(min = 5, max = 100)
        String password
) {}
