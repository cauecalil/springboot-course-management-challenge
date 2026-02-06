package org.cauecalil.coursemanagement.exceptions.domain.professor;

import org.cauecalil.coursemanagement.exceptions.api.ApiErrorCode;
import org.cauecalil.coursemanagement.exceptions.api.NotFoundException;

public class ProfessorNotFoundException extends NotFoundException {
    public ProfessorNotFoundException() {
        super(ApiErrorCode.PROFESSOR_NOT_FOUND, "Professor not found");
    }
}
