package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Response.TeacherSubjectGradeResponse;
import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Subject;
import com.example.SIS_Sample.model.Teacher;
import com.example.SIS_Sample.model.TeacherSubjectGrade;
import com.example.SIS_Sample.repository.CourseRepository;
import com.example.SIS_Sample.repository.SubjectRepository;
import com.example.SIS_Sample.repository.TeacherRepository;
import com.example.SIS_Sample.repository.TeacherSubjectGradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TeacherSubjectGradeServiceImpl implements TeacherSubjectGradeService {

    @Autowired
    private TeacherSubjectGradeRepository teacherSubjectGradeRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Override
    public TeacherSubjectGradeResponse assignTeacherToUnassignedCourse(Long teacherId, Long subjectId, Long courseId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found."));
        if (teacher.getSubject() == null || !teacher.getSubject().getId().equals(subjectId)) {
            throw new RuntimeException("This teacher is not assigned to this subject. Please assign the teacher to this subject first.");
        }

        boolean courseHasTeacherForSubject = teacherSubjectGradeRepository.existsBySubjectIdAndCourseId(subjectId, courseId);
        if (courseHasTeacherForSubject) {
            throw new RuntimeException("This course already has a teacher assigned for the subject.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found."));

        TeacherSubjectGrade teacherSubjectGrade = new TeacherSubjectGrade();
        teacherSubjectGrade.setTeacher(teacher);
        teacherSubjectGrade.setSubject(teacher.getSubject());
        teacherSubjectGrade.setCourse(course);

        teacherSubjectGradeRepository.save(teacherSubjectGrade);

        return new TeacherSubjectGradeResponse(
                teacherSubjectGrade.getId(),
                teacher.getName(),
                teacher.getSubject().getName(),
                course.getName()
        );
    }

    @Override
    public List<TeacherSubjectGrade> findByTeacherId(Long teacherId) {
        return teacherSubjectGradeRepository.findByTeacherId(teacherId);
    }

    @Override
    public List<Map<String, Object>> getTeacherCoursesWithTotalStudents(Long teacherId) {
        List<TeacherSubjectGrade> teacherSubjectGrades = teacherSubjectGradeRepository.findByTeacherId(teacherId);

        return teacherSubjectGrades.stream()
                .map(tsg -> {
                    Long courseId = tsg.getCourse().getId();
                    Long totalStudents = enrollmentService.getTotalStudentsByCourse(courseId);
                    return Map.of(
                            "courseName", (Object) tsg.getCourse().getName(),
                            "subjectName", (Object) tsg.getSubject().getName(),
                            "teacherName", (Object) tsg.getTeacher().getName(),
                            "totalStudents", (Object) totalStudents
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getCoursesWithStudentCountByTeacherId(Long teacherId) {
        List<TeacherSubjectGrade> teacherSubjectGrades = teacherSubjectGradeRepository.findByTeacherId(teacherId);
        return teacherSubjectGrades.stream()
                .map(tsg -> {
                    Long courseId = tsg.getCourse().getId();
                    Long studentCount = enrollmentService.getTotalStudentsByCourse(courseId);
                    return Map.of(
                            "courseName",(Object) tsg.getCourse().getName(),
                            "teacherName",(Object) tsg.getTeacher().getName(),
                            "studentCount",(Object) studentCount
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalCoursesByTeacherId(Long teacherId){
        return teacherSubjectGradeRepository.findByTeacherId(teacherId).size();
    }
}
