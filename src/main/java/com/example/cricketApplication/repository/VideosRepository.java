package com.example.cricketApplication.repository;


import com.example.cricketApplication.models.Videos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideosRepository extends JpaRepository<Videos, Long> {
    // Additional query methods if needed
}

