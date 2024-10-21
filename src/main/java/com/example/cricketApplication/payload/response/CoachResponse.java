package com.example.cricketApplication.payload.response;

import com.example.cricketApplication.models.PractiseSession;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class CoachResponse {
    private Long coachId;
    private String name;
    private String contactNo;
    private String email;
    private String address;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    private String image; // URL or path to the coach's image
    private String description;

    private String username;
    private String password;

}
