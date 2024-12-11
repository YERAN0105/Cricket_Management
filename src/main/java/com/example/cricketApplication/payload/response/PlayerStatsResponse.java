package com.example.cricketApplication.payload.response;

import java.math.BigDecimal;
import java.util.Date;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class PlayerStatsResponse {
    private Long id;

    private String inning; // "1st Inning", "2nd Inning", etc.

    private int runs;
    private int wickets;
    private int fours;
    private int sixers;
    private int fifties;
    private int centuries;
    private int balls;
    private BigDecimal overs;
    private int runsConceded;
    private int noBalls;
    private int wides;
    private int maidens;
    private String howOut;
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;
    private int catches;
    private int stumps;
    private int runOuts;
    private String remarks;

    // Add player details
    private PlayerResponse player;

    // Add match details
    //private MatchResponse match;

    private MatchResponseWithStat match;

}
