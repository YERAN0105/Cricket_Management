package com.example.cricketApplication.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @Temporal(TemporalType.DATE)
    private Date date;

    //private LocalDate date;

    private String venue;
    private String opposition;
    private String tier; // "A", "B", etc.
    private String division; // "Division 1", "Division 2", etc.
    private String type;
    private String umpires;
    private String matchCaptain;
    private String time;
    private String logo;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PlayerStats> playerStats = new HashSet<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MatchSummary> matchSummaries = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

//    @ManyToMany(mappedBy = "matches")
//    private Set<Coach> coaches = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "coach_match",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "coach_id")
    )
    private Set<Coach> coaches = new HashSet<>();

    // Getters and setters
}

