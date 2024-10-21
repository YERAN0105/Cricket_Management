package com.example.cricketApplication.payload.response;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class MatchResponse {
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
    private String under;
    private String logo;
}
