package org.cauecalil.coursemanagement.modules.professor.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.modules.professor.dtos.AuthProfessorRequestDTO;
import org.cauecalil.coursemanagement.modules.professor.dtos.CreateProfessorRequestDTO;
import org.cauecalil.coursemanagement.modules.professor.services.AuthProfessorService;
import org.cauecalil.coursemanagement.modules.professor.services.CreateProfessorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professors")
@RequiredArgsConstructor
public class ProfessorController {
    private final CreateProfessorService createProfessorService;
    private final AuthProfessorService authProfessorService;

    @PostMapping
    public ResponseEntity<Object> createProfessor(@RequestBody @Valid CreateProfessorRequestDTO request) {
        var result = createProfessorService.execute(request);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/auth")
    public ResponseEntity<Object> authProfessor(@RequestBody @Valid AuthProfessorRequestDTO request) {
        var result = authProfessorService.execute(request);
        return ResponseEntity.ok().body(result);
    }
}
