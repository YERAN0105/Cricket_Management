package com.example.cricketApplication.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @NotBlank
    private String name;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Email
    private String email;

    private String contactNo;

    private String battingStyle;
    private String bowlingStyle;

    private String status;

    private String image;

    private String playerRole;


    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PlayerStats> playerStats = new HashSet<>();

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HistoricalPlayerStats> historicalPlayerStats = new HashSet<>();

//    @ManyToMany(mappedBy = "players")
//    private Set<PractiseSession> practiseSessions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(mappedBy = "players")
    private Set<Team> teams = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "membership_id",referencedColumnName = "id",nullable = true)
    private Membership membership;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;

    public Player(String name, String contactNo, String battingStyle, String bowlingStyle, String status, String image,String playerRole,Membership membership,String email,Date dateOfBirth,Role role) {
    this.name = name;
    this.contactNo = contactNo;
    this.battingStyle = battingStyle;
    this.bowlingStyle = bowlingStyle;
    this.status = status;
    this.image = image;
    this.playerRole = playerRole;
    this.membership = membership;
    this.email = email;
    this.dateOfBirth=dateOfBirth;
    this.role=role;
  }


    // Getters and setters
}
