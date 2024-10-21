package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.Match;
import com.example.cricketApplication.payload.response.MatchResponse;
import com.example.cricketApplication.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    // Save a new match
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    // Get a match by ID
//    public Optional<Match> getMatchById(Long matchId) {
//        return matchRepository.findById(matchId);
//    }

    public Match getMatchById(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));
    }


//        public List<MatchResponse> getMatchById(Long matchId) {
//        List<Match> matchList = matchRepository.findById(matchId);
//        List<MatchResponse> matchResponseList = new ArrayList<>();
//        return matchResponseList;
//    }

    // Get all matches
//    public List<Match> getAllMatches() {
//        return matchRepository.findAll();
//    }

    public List<MatchResponse> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        return RefactorResponse(matches);  // Convert to MatchResponse list
    }


    // Delete a match by ID
    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }

    public MatchResponse updateMatch(Long matchId, Match matchDetails) {
        // Fetch the existing match by ID
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

        // Update the match details
        match.setDate(matchDetails.getDate());
        match.setVenue(matchDetails.getVenue());
        match.setOpposition(matchDetails.getOpposition());
        match.setTier(matchDetails.getTier());
        match.setDivision(matchDetails.getDivision());
        match.setType(matchDetails.getType());
        match.setUmpires(matchDetails.getUmpires());
        match.setMatchCaptain(matchDetails.getMatchCaptain());
        match.setTime(matchDetails.getTime());
        match.setLogo(matchDetails.getLogo());

        // Save the updated match
        Match updatedMatch = matchRepository.save(match);

        // Return the updated match wrapped in a MatchResponse
        return RefactorResponse(Collections.singletonList(updatedMatch)).get(0);
    }


    public List<MatchResponse> RefactorResponse(List<Match> matchList) {
        List<MatchResponse> matchResponseList = new ArrayList<>();
        for (Match match : matchList) {
            MatchResponse matchResponse = new MatchResponse();
            matchResponse.setMatchId(match.getMatchId());
            matchResponse.setDate(match.getDate());
            matchResponse.setDivision(match.getDivision());
            matchResponse.setOpposition(match.getOpposition());
            matchResponse.setTier(match.getTier());
            matchResponse.setUmpires(match.getUmpires());
            matchResponse.setVenue(match.getVenue());
            matchResponse.setMatchCaptain(match.getMatchCaptain());
            matchResponse.setTime(match.getTime());
            matchResponse.setType(match.getType());
            matchResponse.setUnder(match.getTeam().getUnder());
            matchResponse.setLogo(match.getLogo());

            matchResponseList.add(matchResponse);

        }
        return matchResponseList;
    }
}
