package com.example.SIS_Sample.repository;

import com.example.SIS_Sample.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByid(Long id);
    List<Student> findByIsActiveTrue();
    List<Student> findByIsActiveFalse();
    List<Student> findByGenderAndIsActiveTrue(String gender);
    @Query("SELECT s FROM Student s WHERE " +
            "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.gender) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.address) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.guardian) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.phone) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(s.user.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "s.isActive = true")
    List<Student> searchActiveStudents(@Param("keyword") String keyword);
    @Query("SELECT s FROM Student s WHERE s.isActive = true ORDER BY s.id DESC")
    List<Student> findTop10ByIsActiveTrue();
    @Query("SELECT YEAR(u.createdAt) AS year, COUNT(s) AS total FROM Student s JOIN s.user u WHERE s.isActive = true GROUP BY YEAR(u.createdAt) ORDER BY YEAR(u.createdAt) DESC")
    List<Object[]> findTotalStudentsByRegistrationYear();
    @Query("SELECT s FROM Student s WHERE s.isActive = true ORDER BY s.name ASC")
    Page<Student> findActiveStudents(Pageable pageable);
    int countByIsActiveTrue();
    int countByGenderAndIsActiveTrue(String gender);
}
