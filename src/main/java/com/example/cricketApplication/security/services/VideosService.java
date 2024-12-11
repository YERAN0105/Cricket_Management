package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.Videos;
import com.example.cricketApplication.repository.VideosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideosService {

    @Autowired
    private VideosRepository videosRepository;

    public List<Videos> getAllVideos() {
        return videosRepository.findAll();
    }

    public Optional<Videos> getVideoById(Long id) {
        return videosRepository.findById(id);
    }

    public Videos createVideo(Videos video) {
        return videosRepository.save(video);
    }

    public Videos updateVideo(Long id, Videos videoDetails) {
        return videosRepository.findById(id).map(video -> {
            video.setTitle(videoDetails.getTitle());
            video.setUrl(videoDetails.getUrl());
            video.setDateTime(videoDetails.getDateTime());
            video.setUpdatedBy(videoDetails.getUpdatedBy());
            video.setUpdatedOn(videoDetails.getUpdatedOn());
            return videosRepository.save(video);
        }).orElseThrow(() -> new EntityNotFoundException("Video not found with id: " + id));
    }

    public void deleteVideo(Long id) {
        videosRepository.deleteById(id);
    }
}
