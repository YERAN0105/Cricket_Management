package com.example.cricketApplication.payload.response;

import com.example.cricketApplication.models.PractiseSession;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import com.example.cricketApplication.models.Coach;

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
    private String status;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
    private String message;
    public CoachResponse(String message) {
        this.message = message;
    }

    // Constructor that takes a Coach object as a parameter
    public CoachResponse(Coach coach) {
        this.coachId = coach.getCoachId();
        this.name = coach.getName();
        this.contactNo = coach.getContactNo();
        this.email = coach.getEmail();
        this.address = coach.getAddress();
        this.dateOfBirth = coach.getDateOfBirth();
        this.image = coach.getImage();
        this.description = coach.getDescription();
        this.username = coach.getUser().getUsername();
        this.password = coach.getUser().getPassword();
        this.status = coach.getStatus();
        this.createdBy = coach.getCreatedBy();
        this.createdOn = coach.getCreatedOn();
        this.updatedBy = coach.getUpdatedBy();
        this.updatedOn = coach.getUpdatedOn();
    }

}
