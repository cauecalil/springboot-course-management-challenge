package org.cauecalil.coursemanagement.exceptions.api;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    public BadRequestException(ApiErrorCode code, String message) {
        super(HttpStatus.BAD_REQUEST, code, message);
    }
}
