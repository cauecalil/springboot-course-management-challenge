package org.cauecalil.coursemanagement.exceptions.domain.professor;

import org.cauecalil.coursemanagement.exceptions.api.ApiErrorCode;
import org.cauecalil.coursemanagement.exceptions.api.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException() {
        super(ApiErrorCode.INVALID_CREDENTIALS, "Invalid credentials");
    }
}
