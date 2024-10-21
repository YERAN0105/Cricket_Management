package com.example.cricketApplication.repository;

import com.example.cricketApplication.models.Official;
import com.example.cricketApplication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfficialRepository extends JpaRepository<Official, Long> {
    Optional<Official> findByUser(User user);
}
