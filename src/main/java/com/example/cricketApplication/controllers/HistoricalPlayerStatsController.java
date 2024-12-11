package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.HistoricalPlayerStats;
import com.example.cricketApplication.payload.response.HistoricalPlayerStatsResponse;
import com.example.cricketApplication.security.services.HistoricalPlayerStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historicalPlayerStats")
public class HistoricalPlayerStatsController {

    private final HistoricalPlayerStatsService statsService;

    @Autowired
    public HistoricalPlayerStatsController(HistoricalPlayerStatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/add")
    public ResponseEntity<HistoricalPlayerStats> addHistoricalPlayerStats(@RequestBody HistoricalPlayerStats stats) {
        return ResponseEntity.ok(statsService.addHistoricalPlayerStats(stats));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricalPlayerStatsResponse> getHistoricalPlayerStatsById(@PathVariable Long id) {
        return ResponseEntity.ok(statsService.getHistoricalPlayerStatsById(id));
    }

    @GetMapping
    public ResponseEntity<List<HistoricalPlayerStatsResponse>> getAllHistoricalPlayerStats() {
        return ResponseEntity.ok(statsService.getAllHistoricalPlayerStats());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoricalPlayerStats(@PathVariable Long id) {
        statsService.deleteHistoricalPlayerStats(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<HistoricalPlayerStatsResponse>> getHistoricalPlayerStatsByPlayerId(@PathVariable Long playerId) {
        return ResponseEntity.ok(statsService.getHistoricalPlayerStatsByPlayerId(playerId));
    }


    @PutMapping("/{id}")
    public ResponseEntity<HistoricalPlayerStatsResponse> updateHistoricalPlayerStats(
            @PathVariable Long id, @RequestBody HistoricalPlayerStats updatedStats) {
        return ResponseEntity.ok(statsService.updateHistoricalPlayerStats(id, updatedStats));
    }
}
