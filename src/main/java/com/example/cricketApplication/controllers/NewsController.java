package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.Image;
import com.example.cricketApplication.models.News;
import com.example.cricketApplication.payload.response.MessageResponse;
import com.example.cricketApplication.payload.response.NewsResponse;
import com.example.cricketApplication.security.WebConfig;
import com.example.cricketApplication.security.services.NewsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/news")
public class NewsController {
    private static final String IMAGE_DIRECTORY = "D:\\upload\\";

    @Autowired
    private NewsService newsService;

//    @GetMapping
//    public List<NewsResponse> getAllNews() {
//        return newsService.getAllNews();
//    }

    @GetMapping
    public List<NewsResponse> getAllNews() {
        // Ensure the news is sorted by createdOn in descending order
        return newsService.getAllNewsSortedByTime();
    }


    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id)
                .map(ResponseEntity::ok) // Return 200 OK with the NewsResponse
                .orElse(ResponseEntity.notFound().build()); // Return 404 Not Found
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> createNews(
            @RequestParam("newsData") String newsData,
            @RequestParam("images") List<MultipartFile> imageFiles) {
        try {
            // Parse the JSON payload
            ObjectMapper objectMapper = new ObjectMapper();
            News news = objectMapper.readValue(newsData, News.class);

            // Save images and associate them with the news item
            Set<Image> images = new HashSet<>();
            for (MultipartFile file : imageFiles) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                String filePath = IMAGE_DIRECTORY + fileName;
                Files.write(Paths.get(filePath), file.getBytes());

                Image image = new Image();
                image.setImageUrl(fileName); // Save only the filename or URL
                image.setNews(news);
                images.add(image);
            }

            news.setImages(images);
            NewsResponse response = newsService.createNews(news);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateNews(
            @PathVariable Long id,
            @RequestParam("newsData") String newsData,
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        try {
            // Parse the JSON payload
            ObjectMapper objectMapper = new ObjectMapper();
            News updatedNews = objectMapper.readValue(newsData, News.class);

            // Call the service method to update news
            NewsResponse newsResponse = newsService.updateNews(id, updatedNews, images);

            return ResponseEntity.ok(newsResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/deleteImages/{newsId}")
    public ResponseEntity<?> deleteImages(@PathVariable Long newsId) {
        // Call the service method to delete images
        newsService.deleteImagesByNewsId(newsId);
        return ResponseEntity.ok(new MessageResponse("Images deleted successfully."));
    }

    @DeleteMapping("/deleteImage/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        try {
            newsService.deleteImageById(imageId);
            return ResponseEntity.ok("Image with ID " + imageId + " has been deleted.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}