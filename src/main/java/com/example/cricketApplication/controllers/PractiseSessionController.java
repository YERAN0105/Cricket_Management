package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.PractiseSession;
import com.example.cricketApplication.payload.response.PracticeSessionResponse;
import com.example.cricketApplication.security.services.PractiseSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/practiseSessions")
public class PractiseSessionController {

    @Autowired
    private PractiseSessionService practiseSessionService;

//    @PostMapping("/add")
//    public ResponseEntity<PractiseSession> addPractiseSession(@RequestBody PractiseSession practiseSession) {
//        PractiseSession savedPractiseSession = practiseSessionService.addPractiseSession(practiseSession);
//        return ResponseEntity.ok(savedPractiseSession);
//    }


    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH')")
    public ResponseEntity<PractiseSession> addPractiseSession(@RequestBody PractiseSession practiseSession) {
        PractiseSession savedPractiseSession = practiseSessionService.addPractiseSession(practiseSession);
        return ResponseEntity.ok(savedPractiseSession);
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<PractiseSession>> getAllPractiseSessions() {
//        List<PractiseSession> practiseSessions = practiseSessionService.getAllPractiseSessions();
//        return ResponseEntity.ok(practiseSessions);
//    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PracticeSessionResponse>> getAllPractiseSessions() {
        List<PracticeSessionResponse> practiseSessions = practiseSessionService.getAllPractiseSessions();
        return ResponseEntity.ok(practiseSessions);
    }

    @GetMapping("/{pracId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<?> getPractiseSessionById(@PathVariable Long pracId) {
        Optional<PractiseSession> practiseSession = practiseSessionService.getPractiseSessionById(pracId);
        if (practiseSession.isPresent()) {
            return ResponseEntity.ok(practiseSession.get());
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Error: PractiseSession not found");
        }
    }

//    @GetMapping("/coach/{coachId}")
//    public ResponseEntity<List<PractiseSession>> getPractiseSessionsByCoachId(@PathVariable Long coachId) {
//        List<PractiseSession> practiseSessions = practiseSessionService.getPractiseSessionsByCoachId(coachId);
//        return ResponseEntity.ok(practiseSessions);
//    }

//    @GetMapping("/under/{under}")
//    public ResponseEntity<List<PractiseSession>> getPractiseSessionsByUnder(@PathVariable String under) {
//        List<PractiseSession> practiseSessions = practiseSessionService.getPractiseSessionsByUnder(under);
//        return ResponseEntity.ok(practiseSessions);
//    }

    @DeleteMapping("/{pracId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH')")
    public ResponseEntity<?> deletePractiseSessionById(@PathVariable Long pracId) {
        try {
            practiseSessionService.deletePractiseSessionById(pracId);
            return ResponseEntity.ok("PractiseSession deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/coach/{coachId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PracticeSessionResponse>> getPractiseSessionsByCoachId(@PathVariable Long coachId) {
        List<PracticeSessionResponse> practiseSessions = practiseSessionService.getPractiseSessionsByCoachId(coachId);
        if (practiseSessions.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content if no sessions found
        }
        return ResponseEntity.ok(practiseSessions); // Return 200 OK with the list of sessions
    }


    @PutMapping("/update/{pracId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH')")
    public ResponseEntity<PracticeSessionResponse> updatePractiseSession(
            @PathVariable Long pracId,
            @RequestBody PractiseSession practiseSessionDetails) {
        try {
            PracticeSessionResponse updatedPractiseSessionResponse = practiseSessionService.updatePractiseSession(pracId, practiseSessionDetails);
            return ResponseEntity.ok(updatedPractiseSessionResponse);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null); // Return appropriate error response
        }
    }

    @GetMapping("/player/{playerId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_COACH', 'ROLE_PLAYER', 'ROLE_OFFICIAL')")
    public ResponseEntity<List<PracticeSessionResponse>> getPractiseSessionsByPlayerId(@PathVariable Long playerId) {
        List<PracticeSessionResponse> practiseSessions = practiseSessionService.getPractiseSessionsByPlayerId(playerId);
        return ResponseEntity.ok(practiseSessions);
    }


}



