package org.cauecalil.coursemanagement.exceptions.domain.course;

import org.cauecalil.coursemanagement.exceptions.api.ApiErrorCode;
import org.cauecalil.coursemanagement.exceptions.api.BadRequestException;

public class InvalidCourseUpdateException extends BadRequestException {
    public InvalidCourseUpdateException(String message) {
        super(ApiErrorCode.INVALID_COURSE_UPDATE, message);
    }
}
