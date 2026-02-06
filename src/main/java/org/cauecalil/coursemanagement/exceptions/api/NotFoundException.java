package org.cauecalil.coursemanagement.exceptions.api;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException(ApiErrorCode code, String message) {
        super(HttpStatus.NOT_FOUND, code, message);
    }
}
