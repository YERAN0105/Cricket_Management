package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.Match;
import com.example.cricketApplication.models.MatchSummary;
import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.payload.response.MatchSummaryResponse;
import com.example.cricketApplication.repository.MatchRepository;
import com.example.cricketApplication.repository.MatchSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchSummaryService {

    @Autowired
    private MatchSummaryRepository matchSummaryRepository;

    @Autowired
    private MatchRepository matchRepository;



    public MatchSummary createMatchSummary(MatchSummary matchSummary) {
        return matchSummaryRepository.save(matchSummary);
    }


    public Optional<MatchSummary> getMatchSummaryById(Long id) {
        return matchSummaryRepository.findById(id);
    }

//    public List<MatchSummary> getAllMatchSummaries() {
//        return matchSummaryRepository.findAll();
//    }
public List<MatchSummaryResponse> getAllMatchSummaries() {
    List<MatchSummary> matchSummaries = matchSummaryRepository.findAll();
    return RefactorResponse(matchSummaries);  // Convert to MatchSummaryResponse list
}


    public void deleteMatchSummaryById(Long id) {
        matchSummaryRepository.deleteById(id);
    }

    // New method to get all match summaries by match ID
    public List<MatchSummaryResponse> getMatchSummariesByMatchId(Long matchId) {
        List<MatchSummary> matchSummaryList = matchSummaryRepository.findByMatchMatchId(matchId);
        return RefactorResponse(matchSummaryList);
    }


    public MatchSummaryResponse updateMatchSummary(Long id, MatchSummary updatedMatchSummary) {
        MatchSummary matchSummary = matchSummaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MatchSummary not found with ID: " + id));

        // Update the match summary fields
        matchSummary.setInning(updatedMatchSummary.getInning());
        matchSummary.setRuns(updatedMatchSummary.getRuns());
        matchSummary.setWickets(updatedMatchSummary.getWickets());
        matchSummary.setOvers(updatedMatchSummary.getOvers());
        matchSummary.setOppositionRuns(updatedMatchSummary.getOppositionRuns());
        matchSummary.setOppositionWickets(updatedMatchSummary.getOppositionWickets());
        matchSummary.setOppositionOvers(updatedMatchSummary.getOppositionOvers());
        matchSummary.setResult(updatedMatchSummary.getResult());
        matchSummary.setUpdatedBy(updatedMatchSummary.getUpdatedBy());
        matchSummary.setUpdatedOn(updatedMatchSummary.getUpdatedOn());

        // Update the related match (optional)
        if (updatedMatchSummary.getMatch() != null) {
            Match match = matchRepository.findById(updatedMatchSummary.getMatch().getMatchId())
                    .orElseThrow(() -> new RuntimeException("Match not found with ID: " + updatedMatchSummary.getMatch().getMatchId()));
            matchSummary.setMatch(match);
        }

        // Save the updated match summary
        MatchSummary savedMatchSummary = matchSummaryRepository.save(matchSummary);

        // Convert to MatchSummaryResponse
        return refactorResponse(savedMatchSummary);
    }

    private MatchSummaryResponse refactorResponse(MatchSummary matchSummary) {
        MatchSummaryResponse response = new MatchSummaryResponse();
        response.setId(matchSummary.getId());
        response.setInning(matchSummary.getInning());
        response.setRuns(matchSummary.getRuns());
        response.setWickets(matchSummary.getWickets());
        response.setOvers(matchSummary.getOvers());
        response.setOppositionRuns(matchSummary.getOppositionRuns());
        response.setOppositionWickets(matchSummary.getOppositionWickets());
        response.setOppositionOvers(matchSummary.getOppositionOvers());
        response.setResult(matchSummary.getResult());
        response.setMatchId(matchSummary.getMatch().getMatchId());  // Get the match ID
        response.setVenue(matchSummary.getMatch().getVenue());  // Get the match venue
        response.setDate(matchSummary.getMatch().getDate());
        response.setType(matchSummary.getMatch().getType());
        response.setOpposition(matchSummary.getMatch().getOpposition());  // Get the opposition team

        // Fetch and set the "under" age group from the team
        response.setUnder(matchSummary.getMatch().getTeam().getUnder());  // Get the Under age group
        response.setLogo(matchSummary.getMatch().getLogo());
        response.setCreatedBy(matchSummary.getCreatedBy());
        response.setUpdatedBy(matchSummary.getUpdatedBy());
        response.setCreatedOn(matchSummary.getCreatedOn());
        response.setUpdatedOn(matchSummary.getUpdatedOn());

        return response;
    }

    private List<MatchSummaryResponse> RefactorResponse(List<MatchSummary> matchSummaryList) {
        List<MatchSummaryResponse> matchSummaryResponseList = new ArrayList<>();
        for (MatchSummary matchSummary : matchSummaryList) {
            MatchSummaryResponse response = new MatchSummaryResponse();
            response.setId(matchSummary.getId());
            response.setInning(matchSummary.getInning());
            response.setRuns(matchSummary.getRuns());
            response.setWickets(matchSummary.getWickets());
            response.setOvers(matchSummary.getOvers());
            response.setOppositionRuns(matchSummary.getOppositionRuns());
            response.setOppositionWickets(matchSummary.getOppositionWickets());
            response.setOppositionOvers(matchSummary.getOppositionOvers());
            response.setResult(matchSummary.getResult());
            response.setMatchId(matchSummary.getMatch().getMatchId());// Get the match ID

            response.setMatchId(matchSummary.getMatch().getMatchId());  // Get the match ID
            response.setVenue(matchSummary.getMatch().getVenue());  // Get the match venue
            response.setDate(matchSummary.getMatch().getDate());
            response.setType(matchSummary.getMatch().getType());
            response.setOpposition(matchSummary.getMatch().getOpposition());  // Get the opposition team

            // Fetch and set the "under" age group from the team
            response.setUnder(matchSummary.getMatch().getTeam().getUnder());  // Get the Under age group
            response.setLogo(matchSummary.getMatch().getLogo());
            response.setTeamId(matchSummary.getMatch().getTeam().getTeamId());
            response.setTeamYear(matchSummary.getMatch().getTeam().getYear());
            response.setCreatedBy(matchSummary.getCreatedBy());
            response.setUpdatedBy(matchSummary.getUpdatedBy());
            response.setCreatedOn(matchSummary.getCreatedOn());
            response.setUpdatedOn(matchSummary.getUpdatedOn());

            matchSummaryResponseList.add(response);

        }
        return matchSummaryResponseList;
    }
}

