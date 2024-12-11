package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.Match;
import com.example.cricketApplication.payload.response.MatchResponse;
import com.example.cricketApplication.security.services.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    // Add a new match
//    @PostMapping("/add")
//    public ResponseEntity<Match> addMatch(@RequestBody Match match) {
//        Match savedMatch = matchService.saveMatch(match);
//        return ResponseEntity.ok(savedMatch);
//    }


//    @PostMapping("/add")
//    public ResponseEntity<Match> addMatch(
//            @RequestParam("matchData") String matchData, // JSON match data as string
//            @RequestParam(value = "logo") MultipartFile logoFile) { // Optional logo image file
//        try {
//            // Parse the match data (JSON) into a Match object
//            ObjectMapper objectMapper = new ObjectMapper();
//            Match match = objectMapper.readValue(matchData, Match.class);
//
//            // Save the match with the logo
//            Match savedMatch = matchService.saveMatch(match, logoFile);
//            return ResponseEntity.ok(savedMatch);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(null);
//        }
//    }

    @PostMapping("/add")
    public ResponseEntity<Match> addMatch(
            @RequestParam("matchData") String matchData, // JSON match data as string
            @RequestParam(value = "logo") MultipartFile logoFile) { // Optional logo image file
        try {
            // Parse the match data (JSON) into a Match object
            ObjectMapper objectMapper = new ObjectMapper();
            Match match = objectMapper.readValue(matchData, Match.class);

            if (logoFile == null || logoFile.isEmpty()) {
                throw new RuntimeException("Logo file is missing");
            }

            // Save the match with the logo
            Match savedMatch = matchService.saveMatch(match, logoFile);
            return ResponseEntity.ok(savedMatch);

        } catch (Exception e) {
            // Log the exception for debugging
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Get a match by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
//        Optional<Match> match = matchService.getMatchById(id);
//        return match.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @GetMapping("/{id}")
    public ResponseEntity<List<MatchResponse>> getMatchById(@PathVariable Long id) {
        Match match = matchService.getMatchById(id);
                //.orElseThrow(() -> new RuntimeException("Match not found with id: " + id));

        // Wrap the match in a list since RefactorResponse expects a list
        List<Match> matchList = Collections.singletonList(match);

        // Call RefactorResponse to convert the match to a MatchResponse
        List<MatchResponse> matchResponse = matchService.RefactorResponse(matchList);

        return ResponseEntity.ok(matchResponse);
    }

    @GetMapping("/{matchId}/hasRequiredSummaries")
    public ResponseEntity<Boolean> hasRequiredMatchSummaries(@PathVariable Long matchId) {
        boolean hasRequiredSummaries = matchService.hasRequiredMatchSummaries(matchId);
        return ResponseEntity.ok(hasRequiredSummaries);
    }



    // Get all matches
//    @GetMapping("/all")
//    public ResponseEntity<List<Match>> getAllMatches() {
//        List<Match> matches = matchService.getAllMatches();
//        return ResponseEntity.ok(matches);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<MatchResponse>> getAllMatches() {
        List<MatchResponse> matchResponses = matchService.getAllMatches();
        return ResponseEntity.ok(matchResponses);
    }


    // Delete a match by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MatchResponse> updateMatch(
            @PathVariable Long id,
            @RequestParam("matchData") String matchData, // Match details as JSON
            @RequestParam(value = "logo", required = false) MultipartFile logoFile // Optional logo file
    ) {
        try {
            // Parse the JSON data into the Match object
            ObjectMapper objectMapper = new ObjectMapper();
            Match matchDetails = objectMapper.readValue(matchData, Match.class);

            // Update the match using the service
            MatchResponse updatedMatch = matchService.updateMatch(id, matchDetails, logoFile);

            return ResponseEntity.ok(updatedMatch);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MatchResponse("Error: " + e.getMessage()));
        }
    }


}

