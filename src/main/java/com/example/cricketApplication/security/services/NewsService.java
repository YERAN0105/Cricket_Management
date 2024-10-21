package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.News;
import com.example.cricketApplication.repository.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }

    public News createNews(News news) {
        return newsRepository.save(news);
    }

    public News updateNews(Long id, News newsDetails) {
        return newsRepository.findById(id).map(news -> {
            news.setHeading(newsDetails.getHeading());
            news.setBody(newsDetails.getBody());
            news.setImageUrl(newsDetails.getImageUrl());
            news.setLink(newsDetails.getLink());
            news.setDateTime(newsDetails.getDateTime());
            return newsRepository.save(news);
        }).orElseThrow(() -> new EntityNotFoundException("News not found with id: " + id));
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}
