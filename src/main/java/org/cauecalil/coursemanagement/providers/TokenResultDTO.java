package org.cauecalil.coursemanagement.providers;

import lombok.Builder;

@Builder
public record TokenResultDTO(
        String accessToken,
        long expiresAt
) {}
