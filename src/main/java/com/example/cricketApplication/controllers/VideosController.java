package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.Videos;
import com.example.cricketApplication.security.services.VideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/videos")
public class VideosController {

    @Autowired
    private VideosService videosService;

    @GetMapping
    public List<Videos> getAllVideos() {
        return videosService.getAllVideos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Videos> getVideoById(@PathVariable Long id) {
        return videosService.getVideoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Videos createVideo(@RequestBody Videos video) {
        return videosService.createVideo(video);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Videos> updateVideo(@PathVariable Long id, @RequestBody Videos videoDetails) {
        return ResponseEntity.ok(videosService.updateVideo(id, videoDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videosService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
}

