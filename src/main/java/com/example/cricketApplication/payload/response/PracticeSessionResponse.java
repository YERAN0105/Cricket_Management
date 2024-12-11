package com.example.cricketApplication.payload.response;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.List;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class PracticeSessionResponse {
    private Long pracId;

    private String venue;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String endTime;
    private String pracType;
    private String teamUnder;
    private int teamYear;
    private long teamId;
    private List<CoachPractiseSession> coaches;
    private String startTime;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
}