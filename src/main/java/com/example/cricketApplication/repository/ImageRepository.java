package com.example.cricketApplication.repository;

import com.example.cricketApplication.models.Image;
import com.example.cricketApplication.models.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByNewsId(Long newsId);
}
