package org.cauecalil.coursemanagement.exceptions.api;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(ApiErrorCode code, String message) {
        super(HttpStatus.UNAUTHORIZED, code, message);
    }
}
