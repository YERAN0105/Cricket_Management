package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.PlayerStats;
import com.example.cricketApplication.payload.response.MessageResponse;
import com.example.cricketApplication.payload.response.PlayerResponse;
import com.example.cricketApplication.payload.response.PlayerStatsResponse;
import com.example.cricketApplication.security.services.PlayerStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/playerStats")
public class PlayerStatsController {

    @Autowired
    private PlayerStatsService playerStatsService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PlayerStats createPlayerStats(@RequestBody PlayerStats playerStats) {
        return playerStatsService.createPlayerStats(playerStats);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<?> getPlayerStatsById(@PathVariable Long id) {
        Optional<PlayerStats> playerStats = playerStatsService.getPlayerStatsById(id);
        if (playerStats.isPresent()) {
            return ResponseEntity.ok(playerStats.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: PlayerStats not found"));
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PlayerStats>> getAllPlayerStats() {
        List<PlayerStats> playerStatsList = playerStatsService.getAllPlayerStats();
        return ResponseEntity.ok(playerStatsList);
    }

    @GetMapping("/player/{playerId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PlayerStats>> getPlayerStatsByPlayerId(@PathVariable Long playerId) {
        List<PlayerStats> playerStatsList = playerStatsService.getPlayerStatsByPlayerId(playerId);
        return ResponseEntity.ok(playerStatsList);
    }

    @GetMapping("/match/{matchId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PlayerStats>> getPlayerStatsByMatchId(@PathVariable Long matchId) {
        List<PlayerStats> playerStatsList = playerStatsService.getPlayerStatsByMatchId(matchId);
        return ResponseEntity.ok(playerStatsList);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deletePlayerStatsById(@PathVariable Long id) {
        try {
            playerStatsService.deletePlayerStatsById(id);
            return ResponseEntity.ok(new MessageResponse("PlayerStats deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }



//
//    @GetMapping("/match")
//    public ResponseEntity<List<PlayerStats>> getPlayerStatsByMatch(
//            @RequestParam("playerId") Long playerId,
//            @RequestParam("matchId") Long matchId) {
//
//        List<PlayerStats> playerStats = playerStatsService.getPlayerStatsByMatch(playerId, matchId);
//        return new ResponseEntity<>(playerStats, HttpStatus.OK);
//    }

    @GetMapping("/match")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PlayerStatsResponse>> getPlayerStatsByMatch(
            @RequestParam("playerId") Long playerId,
            @RequestParam("matchId") Long matchId) {

        List<PlayerStatsResponse> playerStatsResponses = playerStatsService.getPlayerStatsByMatch(playerId, matchId);
        return new ResponseEntity<>(playerStatsResponses, HttpStatus.OK);
    }

    @GetMapping("/type")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PlayerStatsResponse>> getPlayerStatsByMatchType(
            @RequestParam("playerId") Long playerId,
            @RequestParam("matchType") String matchType) {

        List<PlayerStatsResponse> playerStatsResponses = playerStatsService.getPlayerStatsByMatchType(playerId, matchType);
        return new ResponseEntity<>(playerStatsResponses, HttpStatus.OK);
    }

    @Cacheable("playerStatCache")
    @GetMapping("/summery")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<PlayerStatsResponse> getOverallStatByMatchType(
            @RequestParam("playerId") Long playerId,
            @RequestParam("matchType") String matchType)
    {
        List<PlayerStatsResponse> playerStatsResponses = playerStatsService.getPlayerStatsByMatchType(playerId, matchType);

        return new ResponseEntity<PlayerStatsResponse>( calculateStatsByMatchType(playerStatsResponses), HttpStatus.OK);
    }

    private PlayerStatsResponse calculateStatsByMatchType(List<PlayerStatsResponse> playerStatsResponses) {
        PlayerStatsResponse playerStatsResponse = new PlayerStatsResponse();
        for (PlayerStatsResponse playerStatsResponseItem : playerStatsResponses) {
            playerStatsResponse.setRuns(playerStatsResponseItem.getRuns()+playerStatsResponse.getRuns());
        }
        playerStatsResponse.setInning(String.valueOf(playerStatsResponses.size()));
        return playerStatsResponse;
    }


    @GetMapping("/match/player-stats")

    public ResponseEntity<List<PlayerStatsResponse>> getPlayerStatsForMatch(
            @RequestParam("matchId") Long matchId) {
        List<PlayerStatsResponse> playerStatsResponses = playerStatsService.getPlayerStatsForMatch(matchId);
        return new ResponseEntity<>(playerStatsResponses, HttpStatus.OK);
    }


    // New endpoint to get all player stats by player ID
    @GetMapping("/all-stats/{playerId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PlayerStatsResponse>> getAllStatsByPlayerId(@PathVariable Long playerId) {
        List<PlayerStatsResponse> playerStatsResponses = playerStatsService.getAllStatsByPlayerId(playerId);
        return ResponseEntity.ok(playerStatsResponses);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatePlayerStats(@PathVariable Long id, @RequestBody PlayerStats updatedPlayerStats) {
        try {
            PlayerStats playerStats = playerStatsService.updatePlayerStats(id, updatedPlayerStats);
            return ResponseEntity.ok(playerStats);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/addMultiple")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PlayerStats> createMultiplePlayerStats(@RequestBody List<PlayerStats> playerStatsList) {
        return playerStatsService.createMultiplePlayerStats(playerStatsList);
    }



}






