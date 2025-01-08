package com.example.cricketApplication.repository;

import com.example.cricketApplication.models.Admin;
import com.example.cricketApplication.models.Official;
import com.example.cricketApplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUser(User user);
}
