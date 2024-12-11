package com.example.cricketApplication.models;

import jakarta.persistence.*;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    // Many images can be linked to one news item
    @ManyToOne
    @JoinColumn(name = "news_id", nullable = false)
    private News news;
}
