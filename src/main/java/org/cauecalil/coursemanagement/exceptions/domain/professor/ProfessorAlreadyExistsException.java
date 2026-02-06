package org.cauecalil.coursemanagement.exceptions.domain.professor;

import org.cauecalil.coursemanagement.exceptions.api.ApiErrorCode;
import org.cauecalil.coursemanagement.exceptions.api.ConflictException;

public class ProfessorAlreadyExistsException extends ConflictException {
    public ProfessorAlreadyExistsException() {
        super(ApiErrorCode.PROFESSOR_ALREADY_EXISTS, "Professor already exists");
    }
}
