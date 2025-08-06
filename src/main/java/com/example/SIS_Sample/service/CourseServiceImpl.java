package com.example.SIS_Sample.service;

import com.example.SIS_Sample.dto.Request.CourseRequest;
import com.example.SIS_Sample.dto.Response.CourseResponse;
import com.example.SIS_Sample.dto.Response.TeacherResponse;
import com.example.SIS_Sample.model.Course;
import com.example.SIS_Sample.model.Subject;
import com.example.SIS_Sample.model.Teacher;
import com.example.SIS_Sample.repository.CourseRepository;
import com.example.SIS_Sample.repository.SubjectRepository;
import com.example.SIS_Sample.repository.TeacherRepository;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findByIdAndIsActive(id, true)
                .orElseThrow(() -> new IllegalArgumentException("Course not found!"));
        return mapToResponse(course);
    }

    @Override
    public List<CourseResponse> getAllActiveCourses() {
        List<Course> courses = courseRepository.findByIsActive(true);
        return courses.stream().map(this::mapToResponse).toList();
    }

    @Override
    public CourseResponse createCourse(CourseRequest courseRequest) {
        if(courseRequest.getName() == null || courseRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("Course name cannot be null or empty!");
        }
        if(courseRepository.existsByName(courseRequest.getName())) {
            throw new IllegalArgumentException("This Course already exists!");
        }
        Course course = new Course();
        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());

        Course saveCourse = courseRepository.save(course);

        return mapToResponse(saveCourse);
    }

    @Override
    public CourseResponse updateCourse(Long id, CourseRequest courseRequest) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found!"));

        if(courseRequest.getName() != null && !courseRequest.getName().isEmpty()) {
            if(courseRepository.existsByName(courseRequest.getName()) && !course.getName().equals(courseRequest.getName())){
                throw new IllegalArgumentException("This Course already exists!");
            }
            course.setName(courseRequest.getName());
        }
        if(courseRequest.getDescription() != null && !courseRequest.getDescription().isEmpty()) {
            course.setDescription(courseRequest.getDescription());
        }

        Course updatedCourse = courseRepository.save(course);
        return mapToResponse(updatedCourse);
    }

    @Override
    public void assignTeacherToCourse(Long courseId, Long teacherId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found!"));

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found!"));

        if(courseRepository.existsByTeacher(teacher)){
            throw new IllegalArgumentException("This teacher is already assigned to another course!");
        }

        teacher.setCourse(course);
        course.getTeachers().add(teacher);

        teacherRepository.save(teacher);  // Save the owning side (Teacher)
    }

    @Override
    public void assignSubjectsToCourse(Long courseId, List<Long> subjectIds){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found!"));

        List<Subject> subjects = subjectRepository.findAllById(subjectIds);
        if (subjects.isEmpty()) {
            throw new IllegalArgumentException("No valid subjects provided!");
        }

        for (Subject subject : subjects) {
            if (course.getSubjects().contains(subject)) {
                throw new IllegalArgumentException("Subject with ID " + subject.getId() + " is already assigned to the course.");
            }
            course.getSubjects().add(subject);
        }
        courseRepository.save(course);
    }

    @Override
    public List<CourseResponse> getTop10ActiveCourses(){
        List<Course> course = courseRepository.findTop10ByIsActiveTrue();
        return course.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<TeacherResponse> getTeachersByCourseId(Long courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found!"));

        return course.getTeachers().stream()
                .map(this::mapTeacherToTeacherResponse)
                .collect(Collectors.toList());
    }

    @Override
    public int getTotalActiveCourses() {
        return courseRepository.countByIsActive(true);
    }


    @Override
    public void deactivateCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found!"));
        course.setActive(false);
        courseRepository.save(course);
    }

    private CourseResponse mapToResponse(Course course) {
        List<Long> teacherIds = course.getTeachers().stream()
                .map(Teacher::getId)
                .collect(Collectors.toList());
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getDescription(),
                teacherIds,
                course.isActive() ? "Active" : "Inactive"
        );
    }

    private TeacherResponse mapTeacherToTeacherResponse(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getUser().getEmail(),
                teacher.getDob(),
                teacher.getName(),
                teacher.getGender(),
                teacher.getAddress(),
                teacher.getPhone(),
                teacher.getProfilePicture(),
                teacher.getCourse() != null ? teacher.getCourse().getName() : null
        );
    }
}
