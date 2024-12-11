package com.example.cricketApplication.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@Entity
@Table(name = "practise_sessions")
public class PractiseSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pracId;

    private String venue;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String startTime;
    private String endTime;
    private String pracType;

    @ManyToMany
    @JoinTable(
            name = "coach_practise_session",
            joinColumns = @JoinColumn(name = "prac_id"),
            inverseJoinColumns = @JoinColumn(name = "coach_id")
    )
    private Set<Coach> coaches = new HashSet<>();

//    @ManyToMany
//    @JoinTable(
//            name = "player_practise_sessions",
//            joinColumns = @JoinColumn(name = "prac_id"),
//            inverseJoinColumns = @JoinColumn(name = "player_id"))
//    private Set<Player> players = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonBackReference
    private Team team;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;

    // Getters and setters
}

