package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.Image;
import com.example.cricketApplication.models.News;
import com.example.cricketApplication.payload.response.NewsResponse;
import com.example.cricketApplication.repository.ImageRepository;
import com.example.cricketApplication.repository.NewsRepository;
import com.example.cricketApplication.security.WebConfig;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ImageRepository imageRepository;

    private static final String IMAGE_DIRECTORY = "D:\\upload\\";

    // Method to get all news and convert them to NewsResponse
//    public List<NewsResponse> getAllNews() {
//        return newsRepository.findAll().stream()
//                .map(NewsResponse::new) // Convert each News entity to NewsResponse
//                .collect(Collectors.toList());
//    }

    public List<NewsResponse> getAllNewsSortedByTime() {
        return newsRepository.findAll(Sort.by(Sort.Direction.DESC, "createdOn"))
                .stream()
                .map(NewsResponse::new) // Convert each News entity to NewsResponse
                .collect(Collectors.toList());
    }

    public Optional<NewsResponse> getNewsById(Long id) {
        return newsRepository.findById(id).map(NewsResponse::new); // Convert News to NewsResponse
    }

    public NewsResponse createNews(News news) {
        if (news.getImages() != null) {
            news.getImages().forEach(image -> image.setNews(news));
        }
        News savedNews = newsRepository.save(news);
        return new NewsResponse(savedNews);
    }

    public void deleteImageById(Long imageId) {
        if (imageRepository.existsById(imageId)) {
            imageRepository.deleteById(imageId);
        } else {
            throw new IllegalArgumentException("Image with ID " + imageId + " not found.");
        }
    }

//    @Transactional
//    public NewsResponse updateNews(Long newsId, News updatedNews) {
//        // Retrieve the existing news item
//        News existingNews = newsRepository.findById(newsId)
//                .orElseThrow(() -> new ResourceNotFoundException("News not found with id: " + newsId));
//
//        // Update the details of the existing news with the new values
//        existingNews.setHeading(updatedNews.getHeading());
//        existingNews.setBody(updatedNews.getBody());
//        existingNews.setLink(updatedNews.getLink());
//        existingNews.setAuthor(updatedNews.getAuthor());
//        existingNews.setCreatedBy(updatedNews.getCreatedBy());
//        existingNews.setUpdatedBy(updatedNews.getUpdatedBy());
//        existingNews.setUpdatedOn(new Date());
//
//        // Handle the images
//        Set<Image> existingImages = new HashSet<>(existingNews.getImages());
//        Set<Image> newImages = updatedNews.getImages() != null ? updatedNews.getImages() : new HashSet<>();
//
//        // Remove images that are no longer present in the new set
//        existingImages.stream()
//                .filter(existingImage -> newImages.stream()
//                        .noneMatch(newImage -> newImage.getId() != null && newImage.getId().equals(existingImage.getId())))
//                .forEach(imageRepository::delete);
//
//        // Update or add each new image
//        newImages.forEach(newImage -> {
//            if (newImage.getId() != null) {
//                // Update the existing image
//                imageRepository.findById(newImage.getId()).ifPresent(existingImage -> {
//                    existingImage.setImageUrl(newImage.getImageUrl());
//                    imageRepository.save(existingImage);
//                });
//            } else {
//                // Add new image and associate it with the news
//                newImage.setNews(existingNews);
//                imageRepository.save(newImage);
//            }
//        });
//
//        // Save the updated news item with the modified images
//        News savedNews = newsRepository.save(existingNews);
//        return new NewsResponse(savedNews);
//    }
    @Transactional
    public NewsResponse updateNews(Long id, News updatedNews, List<MultipartFile> imageFiles) {
        return newsRepository.findById(id).map(existingNews -> {
            // Update basic details
            existingNews.setHeading(updatedNews.getHeading());
            existingNews.setBody(updatedNews.getBody());
            existingNews.setLink(updatedNews.getLink());
            existingNews.setAuthor(updatedNews.getAuthor());
            existingNews.setUpdatedBy(updatedNews.getUpdatedBy());
            existingNews.setUpdatedOn(updatedNews.getUpdatedOn());

            // Handle images
            if (imageFiles != null && !imageFiles.isEmpty()) {
                // Delete old images
                //imageRepository.deleteByNewsId(existingNews.getId());
                //existingNews.getImages().clear();

                // Add new images
                for (MultipartFile file : imageFiles) {
                    try {
                        // Save the image to the filesystem
                        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                        String imagePath = IMAGE_DIRECTORY + fileName;
                        Files.write(Paths.get(imagePath), file.getBytes());

                        // Create and associate the image with the news
                        Image image = new Image();
                        image.setImageUrl(fileName); // Save only the file name
                        image.setNews(existingNews);
                        existingNews.getImages().add(image);

                    } catch (IOException e) {
                        throw new RuntimeException("Error saving image file: " + file.getOriginalFilename(), e);
                    }
                }
            }

            // Save the updated news entity
            News savedNews = newsRepository.save(existingNews);
            return new NewsResponse(savedNews);

        }).orElseThrow(() -> new EntityNotFoundException("News not found with id: " + id));
    }


    @Transactional
    public void deleteImagesByNewsId(Long newsId) {
        imageRepository.deleteByNewsId(newsId);
    }


    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }
}
