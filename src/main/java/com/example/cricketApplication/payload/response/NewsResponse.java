package com.example.cricketApplication.payload.response;

import com.example.cricketApplication.models.News;
import com.example.cricketApplication.models.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.exolab.castor.types.DateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsResponse {
    private Long id;
    private String heading;
    private String body;
    private String link;
    private String author;
    private String createdBy;
    //    private Date createdOn;
    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    private String updatedBy;
    //private Date updatedOn;
    private List<String> imageUrls; // Includes both imageId and URL
    private List<ImageResponse> images;
    private String timeAgo;

    public NewsResponse(News news) {
        this.id = news.getId();
        this.heading = news.getHeading();
        this.body = news.getBody();
        this.link = news.getLink();
        this.author = news.getAuthor();
        this.updatedBy = news.getUpdatedBy();
        this.updatedOn = news.getUpdatedOn();
        this.createdOn = news.getCreatedOn();
        this.createdBy = news.getCreatedBy();
        this.timeAgo = calculateTimeAgo(news.getCreatedOn());

        // Generate URLs for images
//        this.imageUrls = news.getImages().stream()
//                .map(image -> ServletUriComponentsBuilder.fromCurrentContextPath()
//                        .path("/images/")
//                        .path(image.getImageUrl())
//                        .toUriString())
//                .collect(Collectors.toList());


        // Generate URLs and map imageId
        this.images = news.getImages().stream()
                .map(image -> new ImageResponse(
                        image.getId(),
                        ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/images/")
                                .path(image.getImageUrl())
                                .toUriString()
                ))
                .collect(Collectors.toList());
    }


    private String calculateTimeAgo(LocalDateTime dateTime) {
        if (dateTime == null) return "Unknown";

        Duration duration = Duration.between(dateTime, LocalDateTime.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) return seconds + " seconds ago";
        long minutes = seconds / 60;
        if (minutes < 60) return minutes + " minutes ago";
        long hours = minutes / 60;
        if (hours < 24) return hours + " hours ago";
        long days = hours / 24;
        return days + " days ago";
    }
}
