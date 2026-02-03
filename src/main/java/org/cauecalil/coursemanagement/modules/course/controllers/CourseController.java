package org.cauecalil.coursemanagement.modules.course.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cauecalil.coursemanagement.modules.course.dtos.CreateCourseRequestDTO;
import org.cauecalil.coursemanagement.modules.course.dtos.EditCourseRequestDTO;
import org.cauecalil.coursemanagement.modules.course.dtos.FindCoursesQueryDTO;
import org.cauecalil.coursemanagement.modules.course.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CreateCourseService createCourseService;
    private final FindCoursesService findCoursesService;
    private final EditCourseService editCourseService;
    private final DeleteCourseService deleteCourseService;
    private final ToggleCourseActiveService toggleCourseActiveService;

    @PostMapping
    public ResponseEntity<Object> createCourse(@RequestBody @Valid CreateCourseRequestDTO request) {
        var result = createCourseService.execute(request);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public ResponseEntity<Object> findCourses(@ModelAttribute FindCoursesQueryDTO query) {
        var result = findCoursesService.execute(query);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editCourse(@PathVariable UUID id, @RequestBody @Valid EditCourseRequestDTO request) {
        var result = editCourseService.execute(id, request);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable UUID id) {
        deleteCourseService.execute(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/active")
    public ResponseEntity<Object> toggleCourseActive(@PathVariable UUID id) {
        var result = toggleCourseActiveService.execute(id);
        return ResponseEntity.ok().body(result);
    }
}
