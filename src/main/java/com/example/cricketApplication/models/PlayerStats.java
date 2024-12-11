package com.example.cricketApplication.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@Entity
@Table(name = "player_stats")
public class PlayerStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inning; // "1st Inning", "2nd Inning", etc.

    private int runs;
    private int fours;
    private int sixers;
    private int balls;
    private String howOut;
    private int fifties;
    private int centuries;
    private int wickets;
    private BigDecimal overs;
    private int runsConceded;
    private int noBalls;
    private int wides;
    private int maidens;
    private int catches;
    private int stumps;
    private int runOuts;
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;


    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;

    // Getters and setters
}
