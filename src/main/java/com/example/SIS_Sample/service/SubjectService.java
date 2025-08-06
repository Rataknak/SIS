package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Request.SubjectRequest;
import com.example.SIS_Sample.dto.Response.SubjectResponse;

import java.util.List;

public interface SubjectService {
    SubjectResponse create(SubjectRequest subjectRequest);
    SubjectResponse update(Long id, SubjectRequest subjectRequest);
    SubjectResponse getById(Long id);
    List<SubjectResponse> getTop10Subjects();
    List<SubjectResponse> getSubjectsNotAssignedToCourse(Long courseId);
    List<SubjectResponse> getSubjectsAssignedToCourse(Long courseId);

    void assignTeachersToSubject(Long subjectId, Long teacherId);

}
