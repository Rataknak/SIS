package com.example.SIS_Sample.repository;

import com.example.SIS_Sample.dto.Response.SimplifiedCourseResponse;
import com.example.SIS_Sample.model.Student;
import com.example.SIS_Sample.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserId(Long userId);

    @Query("SELECT t FROM Teacher t WHERE t.user.isActive = true")
    List<Teacher> findActiveTeachers();

    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.user.isActive = true")
    int countActiveTeachers();

    @Query("SELECT t FROM Teacher t WHERE " +
            "(LOWER(t.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.gender) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "t.user.isActive = true")
    List<Teacher> SearchActiveTeachers(@Param("keyword") String keyword);


}
