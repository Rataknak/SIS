package com.example.SIS_Sample.repository;

import com.example.SIS_Sample.model.Admin;
import com.example.SIS_Sample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByUserId(Long userId);
}
