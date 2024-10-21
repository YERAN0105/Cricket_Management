package com.example.cricketApplication.payload.response;

import com.example.cricketApplication.models.Membership;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class PlayerResponse {

    private Long playerId;
    private String name;
    private Date dateOfBirth;
    private String email;
    private String contactNo;
    private String battingStyle;
    private String bowlingStyle;
    private String status;
    private String image;
    private String playerRole;
    private String startDate;
    private String endDate;
    private String password;
    private String username;

    // Add a list for 'under' values from the teams
    private List<String> teamsUnder;
}
