package com.example.cricketApplication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@Entity
@Table(name = "coaches")
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coachId;

    @NotBlank
    private String name;

    private String contactNo;
    private String email;
    private String address;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    private String image; // URL or path to the coach's image
    private String description;
    private String status;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;

    @ManyToMany(mappedBy = "coaches")
    private Set<PractiseSession> practiseSessions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    //@OneToOne(fetch = FetchType.LAZY)
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

//    @ManyToMany
//    @JoinTable(
//            name = "coach_match",
//            joinColumns = @JoinColumn(name = "coach_id"),
//            inverseJoinColumns = @JoinColumn(name = "match_id")
//    )
//    private Set<Match> matches = new HashSet<>();

    @ManyToMany(mappedBy = "coaches")
    private Set<Match> matches = new HashSet<>();

    // Getters and setters
}

