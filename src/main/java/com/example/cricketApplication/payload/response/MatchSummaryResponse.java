package com.example.cricketApplication.payload.response;

import com.example.cricketApplication.models.Team;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchSummaryResponse {
    private Long id;
    private String inning;
    private int runs;
    private int wickets;
    private int overs;
    private int oppositionRuns;
    private int oppositionWickets;
    private int oppositionOvers;
    private String result;
    private Long matchId; // Match ID for reference
    private String venue;  // New field for venue
    private String opposition;  // New field for opposition
    private Date date;
    private String type;
    private String under;
    private String logo;

}
