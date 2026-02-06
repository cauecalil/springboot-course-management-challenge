package org.cauecalil.coursemanagement.exceptions.api;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {
    public ConflictException(ApiErrorCode code, String message) {
        super(HttpStatus.CONFLICT, code, message);
    }
}
