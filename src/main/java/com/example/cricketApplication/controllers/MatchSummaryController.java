package com.example.cricketApplication.controllers;
import com.example.cricketApplication.exceptions.InvalidMatchSummaryException;
import com.example.cricketApplication.exceptions.MatchNotFoundException;
import com.example.cricketApplication.models.MatchSummary;
import com.example.cricketApplication.payload.response.MatchSummaryResponse;
import com.example.cricketApplication.payload.response.MessageResponse;

import com.example.cricketApplication.security.services.MatchSummaryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/matchSummary")
public class MatchSummaryController {

    @Autowired
    private MatchSummaryService matchSummaryService;


    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MatchSummary createMatchSummary(@RequestBody MatchSummary matchSummary) {
        return matchSummaryService.createMatchSummary(matchSummary);
    }

//    @PostMapping("/add")
//    public MatchSummary createMatchSummary(
//            @RequestPart("matchSummary") String matchSummaryJson,
//            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
//        // Parse the JSON string into a MatchSummary object
//        ObjectMapper objectMapper = new ObjectMapper();
//        MatchSummary matchSummary;
//        try {
//            matchSummary = objectMapper.readValue(matchSummaryJson, MatchSummary.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Error parsing match summary JSON: " + e.getMessage(), e);
//        }
//
//        return matchSummaryService.createMatchSummary(matchSummary, imageFile);
//    }


    @GetMapping("/{id}")

    public ResponseEntity<?> getMatchSummaryById(@PathVariable Long id) {
        Optional<MatchSummary> matchSummary = matchSummaryService.getMatchSummaryById(id);
        if (matchSummary.isPresent()) {
            return ResponseEntity.ok(matchSummary.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: MatchSummary not found"));
        }
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<MatchSummary>> getAllMatchSummaries() {
//        List<MatchSummary> matchSummaries = matchSummaryService.getAllMatchSummaries();
//        return ResponseEntity.ok(matchSummaries);
//    }

    @GetMapping("/all")

    public ResponseEntity<List<MatchSummaryResponse>> getAllMatchSummaries() {
        List<MatchSummaryResponse> matchSummaryResponses = matchSummaryService.getAllMatchSummaries();
        return ResponseEntity.ok(matchSummaryResponses);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteMatchSummaryById(@PathVariable Long id) {
        try {
            matchSummaryService.deleteMatchSummaryById(id);
            return ResponseEntity.ok(new MessageResponse("MatchSummary deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/match/{matchId}")

    public ResponseEntity<List<MatchSummaryResponse>> getMatchSummariesByMatchId(@PathVariable Long matchId) {
        List<MatchSummaryResponse> matchSummaries = matchSummaryService.getMatchSummariesByMatchId(matchId);
        if (!matchSummaries.isEmpty()) {
            return ResponseEntity.ok(matchSummaries);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ArrayList<>()); // or some meaningful error response
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateMatchSummary(@PathVariable Long id, @RequestBody MatchSummary matchSummary) {
        try {
            MatchSummaryResponse updatedMatchSummary = matchSummaryService.updateMatchSummary(id, matchSummary);
            return ResponseEntity.ok(updatedMatchSummary);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}








