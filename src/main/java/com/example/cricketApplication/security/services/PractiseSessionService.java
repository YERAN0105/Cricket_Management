package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.PractiseSession;
import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.payload.response.CoachPractiseSession;
import com.example.cricketApplication.payload.response.PracticeSessionResponse;
import com.example.cricketApplication.repository.PractiseSessionRepository;
import com.example.cricketApplication.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PractiseSessionService {

    @Autowired
    private PractiseSessionRepository practiseSessionRepository;

    @Autowired
    private TeamRepository teamRepository;

//    public PractiseSession addPractiseSession(PractiseSession practiseSession) {
//        return practiseSessionRepository.save(practiseSession);
//    }

    public PractiseSession addPractiseSession(PractiseSession practiseSession) {
        // Fetch the team based on the teamId from the practiseSession
        Team team = teamRepository.findById(practiseSession.getTeam().getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Set the team for the practise session
        practiseSession.setTeam(team);

        // Save and return the practice session
        return practiseSessionRepository.save(practiseSession);
    }

//    public List<PractiseSession> getAllPractiseSessions() {
//        return practiseSessionRepository.findAll();
//    }

//    public List<PracticeSessionResponse> getAllPractiseSessions() {
//        List<PractiseSession> practiceSessions =  practiseSessionRepository.findAll();
//        return refactorResponse(practiceSessions);
//    }

    public List<PracticeSessionResponse> getAllPractiseSessions() {
        List<PractiseSession> practiseSessions = practiseSessionRepository.findAll();

        // Map each PractiseSession to PracticeSessionResponse using the refactorResponse method
        return practiseSessions.stream()
                .map(this::refactorResponse)  // Use method reference to convert each PractiseSession
                .collect(Collectors.toList());
    }

    public Optional<PractiseSession> getPractiseSessionById(Long pracId) {
        return practiseSessionRepository.findById(pracId);
    }

//    public List<PractiseSession> getPractiseSessionsByCoachId(Long coachId) {
//        return practiseSessionRepository.findByAssignedCoach_CoachId(coachId);
//    }

//    public List<PractiseSession> getPractiseSessionsByUnder(String under) {
//        return practiseSessionRepository.findByUnder(under);
//    }

    public void deletePractiseSessionById(Long pracId) {
        practiseSessionRepository.deleteById(pracId);
    }


    // Method to get all practice sessions for a specific coach ID
    public List<PracticeSessionResponse> getPractiseSessionsByCoachId(Long coachId) {
        List<PractiseSession> practiceSessions =  practiseSessionRepository.findAllByCoachId(coachId);
        return RefactorResponse(practiceSessions);
    }


    public PracticeSessionResponse updatePractiseSession(Long pracId, PractiseSession practiseSessionDetails) {
        // Fetch the existing PractiseSession by ID
        PractiseSession existingPractiseSession = practiseSessionRepository.findById(pracId)
                .orElseThrow(() -> new RuntimeException("PractiseSession not found with ID: " + pracId));

        // Update the fields
        existingPractiseSession.setVenue(practiseSessionDetails.getVenue());
        existingPractiseSession.setDate(practiseSessionDetails.getDate());
        existingPractiseSession.setStarTime(practiseSessionDetails.getStarTime());
        existingPractiseSession.setEndTime(practiseSessionDetails.getEndTime());
        existingPractiseSession.setPracType(practiseSessionDetails.getPracType());

        // Update coaches
        if (practiseSessionDetails.getCoaches() != null) {
            existingPractiseSession.setCoaches(practiseSessionDetails.getCoaches());
        }

        // Update the team if provided
        if (practiseSessionDetails.getTeam() != null) {
            Team team = teamRepository.findById(practiseSessionDetails.getTeam().getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with ID: " + practiseSessionDetails.getTeam().getTeamId()));
            existingPractiseSession.setTeam(team);
        }

        // Save the updated PractiseSession
        PractiseSession updatedPractiseSession = practiseSessionRepository.save(existingPractiseSession);

        // Return the refactored response
        return refactorResponse(updatedPractiseSession);
    }


    private PracticeSessionResponse refactorResponse(PractiseSession practiseSession) {
        PracticeSessionResponse response = new PracticeSessionResponse();
        response.setPracId(practiseSession.getPracId());
        response.setVenue(practiseSession.getVenue());
        response.setDate(practiseSession.getDate());
        response.setStarTime(practiseSession.getStarTime());
        response.setEndTime(practiseSession.getEndTime());
        response.setPracType(practiseSession.getPracType());
        response.setTeamUnder(practiseSession.getTeam().getUnder());

        // Set coaches in the response
        List<CoachPractiseSession> coachResponses = practiseSession.getCoaches().stream()
                .map(coach -> new CoachPractiseSession(coach.getCoachId(), coach.getName())) // Assuming 'getName()' exists in Coach entity
                .collect(Collectors.toList());
        response.setCoaches(coachResponses);

        return response;
    }




    public List<PracticeSessionResponse> RefactorResponse(List<PractiseSession> PractiseSessionList) {
        List<PracticeSessionResponse> practiceSessionResponseList = new ArrayList<>();
        for (PractiseSession practiceSession : PractiseSessionList){
            PracticeSessionResponse practiceSessionResponse = new PracticeSessionResponse();
            practiceSessionResponse.setPracId(practiceSession.getPracId());
            practiceSessionResponse.setVenue(practiceSession.getVenue());
            practiceSessionResponse.setDate(practiceSession.getDate());
            practiceSessionResponse.setStarTime(practiceSession.getStarTime());
            practiceSessionResponse.setEndTime(practiceSession.getEndTime());
            practiceSessionResponse.setPracType(practiceSession.getPracType());
            practiceSessionResponse.setTeamUnder(practiceSession.getTeam().getUnder());
            practiceSessionResponseList.add(practiceSessionResponse);

        }
        return practiceSessionResponseList;
    }
}

