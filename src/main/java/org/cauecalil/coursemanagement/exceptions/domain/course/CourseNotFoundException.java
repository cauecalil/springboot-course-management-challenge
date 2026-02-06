package org.cauecalil.coursemanagement.exceptions.domain.course;

import org.cauecalil.coursemanagement.exceptions.api.ApiErrorCode;
import org.cauecalil.coursemanagement.exceptions.api.NotFoundException;

public class CourseNotFoundException extends NotFoundException {
    public CourseNotFoundException() {
        super(ApiErrorCode.COURSE_NOT_FOUND, "Course not found");
    }
}
