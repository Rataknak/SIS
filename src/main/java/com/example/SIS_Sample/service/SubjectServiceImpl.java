package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Request.SubjectRequest;
import com.example.SIS_Sample.dto.Response.SubjectResponse;
import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Subject;
import com.example.SIS_Sample.model.Teacher;
import com.example.SIS_Sample.repository.CourseRepository;
import com.example.SIS_Sample.repository.SubjectRepository;
import com.example.SIS_Sample.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public SubjectResponse create(SubjectRequest subjectRequest) {
        if (subjectRequest.getName() == null || subjectRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be null or empty!");
        }

        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        subject.setDescription(subjectRequest.getDescription());


        Subject savedSubject = subjectRepository.save(subject);
        return mapToResponse(savedSubject);
    }

    @Override
    public SubjectResponse update(Long id, SubjectRequest subjectRequest) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found!"));

        if (subjectRequest.getName() == null || subjectRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be null or empty!");
        }

        subject.setName(subjectRequest.getName());
        subject.setCode(subjectRequest.getCode());
        subject.setDescription(subjectRequest.getDescription());
        Subject updatedSubject = subjectRepository.save(subject);
        return mapToResponse(updatedSubject);
    }

    @Override
    public SubjectResponse getById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found!"));
        return mapToResponse(subject);
    }

    @Override
    public List<SubjectResponse> getTop10Subjects(){
        List<Subject> subjects = subjectRepository.findTop10Subject();
        return subjects.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<SubjectResponse> getSubjectsNotAssignedToCourse(Long courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found!"));
        List<Subject> subjects = subjectRepository.findSubjectsNotAssignedToCourse(course);
        return subjects.stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<SubjectResponse> getSubjectsAssignedToCourse(Long courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found!"));
        List<Subject> subjects = subjectRepository.findSubjectsByCourseId(courseId);
        return subjects.stream().map(this::mapToResponse).toList();
    }

    private SubjectResponse mapToResponse(Subject subject) {
        SubjectResponse response = new SubjectResponse();
        response.setId(subject.getId());
        response.setName(subject.getName());
        response.setCode(subject.getCode());
        response.setDescription(subject.getDescription());
        response.setActive(subject.isActive());
        return response;
    }




    @Override
    public void assignTeachersToSubject(Long subjectId, Long teacherId){
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found!"));
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found!"));

        if (subject.getTeachers().contains(teacher)) {
            throw new IllegalArgumentException("This teacher is already assigned to the subject!");
        }
        subject.setTeachers(subject.getTeachers() != null ? subject.getTeachers() : new ArrayList<>());
        teacher.setSubject(subject);
        subjectRepository.save(subject);
        teacherRepository.save(teacher);

    }

}
