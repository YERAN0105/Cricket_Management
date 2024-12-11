package com.example.cricketApplication.payload.response;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class HistoricalPlayerStatsResponse {
    private Long id;
    private String inning;
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
    private int catches;
    private int stumps;
    private int runOuts;
    private String matchType;
    private Long playerId;  // Assuming player ID is enough for the response
    private String createdBy;
    private Date createdOn;
    private String updatedBy;
    private Date updatedOn;

}
