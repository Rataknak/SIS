package com.example.SIS_Sample.controller;

import com.example.SIS_Sample.dto.Request.SubjectRequest;
import com.example.SIS_Sample.dto.Response.SubjectResponse;
import com.example.SIS_Sample.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectResponse> createSubject(@RequestBody SubjectRequest subjectRequest) {
        SubjectResponse subjectResponse = subjectService.create(subjectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> updateSubject(@PathVariable Long id, @RequestBody SubjectRequest subjectRequest) {
        SubjectResponse subjectResponse = subjectService.update(id, subjectRequest);
        return ResponseEntity.ok(subjectResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable Long id) {
        SubjectResponse subjectResponse = subjectService.getById(id);
        return ResponseEntity.ok(subjectResponse);
    }

    @GetMapping("/top10")
    public ResponseEntity<List<SubjectResponse>> getTop10Subjects() {
        List<SubjectResponse> subjectResponses = subjectService.getTop10Subjects();
        return ResponseEntity.ok(subjectResponses);
    }
    @GetMapping("/notAssigned/{courseId}")
    public ResponseEntity<List<SubjectResponse>> getSubjectsNotAssignedToCourse(@PathVariable Long courseId) {
        List<SubjectResponse> subjectResponses = subjectService.getSubjectsNotAssignedToCourse(courseId);
        return ResponseEntity.ok(subjectResponses);
    }

    @GetMapping("/assigned/{courseId}")
    public ResponseEntity<List<SubjectResponse>> getSubjectsAssignedToCourse(@PathVariable Long courseId) {
        List<SubjectResponse> subjectResponses = subjectService.getSubjectsAssignedToCourse(courseId);
        return ResponseEntity.ok(subjectResponses);
    }

    @PostMapping("/{subjectId}/assignTeacher/{teacherId}")
    public ResponseEntity<String> assignTeachersToSubject(@PathVariable Long subjectId, @PathVariable Long teacherId) {
        try {
            subjectService.assignTeachersToSubject(subjectId, teacherId);
            return ResponseEntity.status(HttpStatus.OK).body("Teacher assigned to subject successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
