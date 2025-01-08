package com.example.cricketApplication.payload.response;

import java.util.Date;
import java.util.List;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class TeamResponse {

    private Long teamId;

    private String under; // "Under 13", "Under 15", etc.
    private int year;
    private String captain;
    private String ViceCaptain;

//    private Long captain;
//    private Long ViceCaptain;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
    private List<PlayerResponse> players;
}
