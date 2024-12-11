package com.example.cricketApplication.payload.response;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class MatchResponseWithStat {
    private String matchId;
    private String type;
    private String under;
    private int year;

}
