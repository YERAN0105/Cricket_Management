package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.Coach;
import com.example.cricketApplication.payload.response.CoachResponse;
import com.example.cricketApplication.payload.response.MessageResponse;
import com.example.cricketApplication.security.services.CoachService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/coaches")
public class CoachController {

    @Autowired
    private CoachService coachService;

    @PostMapping("/add")
    public ResponseEntity<Coach> addCoach(@RequestBody Coach coach) {
        Coach savedCoach = coachService.addCoach(coach);
        return ResponseEntity.ok(savedCoach);
    }

    @GetMapping("/{coachId}")
    public ResponseEntity<?> getCoachById(@PathVariable Long coachId) {
        try {
            CoachResponse coach = coachService.getCoachById(coachId);
            return ResponseEntity.ok(coach);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CoachResponse>> getAllCoaches() {
        List<CoachResponse> coaches = coachService.getAllCoaches();
        return ResponseEntity.ok(coaches);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCoachByUserId(@PathVariable Long userId) {
        Optional<Coach> coach = coachService.getCoachByUserId(userId);
        if (coach.isPresent()) {
            return ResponseEntity.ok(coach.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: Coach not found"));
        }
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<Coach>> getCoachesByRoleId(@PathVariable Long roleId) {
        List<Coach> coaches = coachService.getCoachesByRoleId(roleId);
        return ResponseEntity.ok(coaches);
    }

    @DeleteMapping("/{coachId}")
    public ResponseEntity<?> deleteCoachById(@PathVariable Long coachId) {
        try {
            coachService.deleteCoachById(coachId);
            return ResponseEntity.ok(new MessageResponse("Coach deleted successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }



    @PutMapping("/{coachId}")
    public ResponseEntity<?> updateCoach(@PathVariable Long coachId, @RequestBody Coach coachDetails) {
        try {
            CoachResponse updatedCoach = coachService.updateCoach(coachId, coachDetails);
            return ResponseEntity.ok(updatedCoach);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

}

