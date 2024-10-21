package com.example.cricketApplication.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String heading;

    @Column(columnDefinition = "TEXT")
    private String body; // To store larger text for the news body

    private String imageUrl;

    private String link;

    @CreationTimestamp
    private LocalDateTime dateTime;

    private String author;

}
