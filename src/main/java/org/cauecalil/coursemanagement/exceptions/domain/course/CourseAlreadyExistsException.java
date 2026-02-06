package org.cauecalil.coursemanagement.exceptions.domain.course;

import org.cauecalil.coursemanagement.exceptions.api.ApiErrorCode;
import org.cauecalil.coursemanagement.exceptions.api.ConflictException;

public class CourseAlreadyExistsException extends ConflictException {
    public CourseAlreadyExistsException() {
        super(ApiErrorCode.COURSE_ALREADY_EXISTS, "Course already exists");
    }
}
