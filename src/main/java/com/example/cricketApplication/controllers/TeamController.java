package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.payload.response.PlayerResponse;
import com.example.cricketApplication.payload.response.TeamResponse;
import com.example.cricketApplication.security.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/teams")
public class TeamController {

    @Autowired
    private TeamService teamService;

//    @PostMapping("/add")
//    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
//        Team savedTeam = teamService.addTeam(team);
//        return ResponseEntity.ok(savedTeam);
//    }

    @PostMapping("/add")
    public ResponseEntity<?> addTeam(@RequestBody Team team) {
        try {
            Team savedTeam = teamService.addTeam(team);
            return ResponseEntity.ok(savedTeam);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        List<TeamResponse> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeamById(@PathVariable Long id) {
        teamService.deleteTeamById(id);
        return ResponseEntity.ok("Team deleted successfully!");
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
//        return teamService.updateTeam(id, teamDetails)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
        return teamService.updateTeam(id, teamDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/players")
    public ResponseEntity<List<PlayerResponse>> getPlayersByTeamId(@PathVariable Long id) {
        List<PlayerResponse> players = teamService.getPlayersByTeamId(id);
        return ResponseEntity.ok(players);
    }




}
