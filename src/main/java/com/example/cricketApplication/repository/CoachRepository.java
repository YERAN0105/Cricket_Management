package com.example.cricketApplication.repository;

import com.example.cricketApplication.models.Coach;
import com.example.cricketApplication.models.ERole;
import com.example.cricketApplication.models.Role;
import com.example.cricketApplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    List<Coach> findByRole_Id(Long roleId);
    Optional<Coach> findByUser_Id(Long userId);

    Optional<Object> findByUser(User user);
    boolean existsByEmail(String email);
}
