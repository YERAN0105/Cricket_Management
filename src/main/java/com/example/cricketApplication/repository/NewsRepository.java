package com.example.cricketApplication.repository;

import com.example.cricketApplication.models.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // Additional query methods if needed
}
