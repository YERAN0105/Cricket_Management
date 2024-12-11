package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.PractiseSession;
import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.payload.response.CoachPractiseSession;
import com.example.cricketApplication.payload.response.PracticeSessionResponse;
import com.example.cricketApplication.repository.PractiseSessionRepository;
import com.example.cricketApplication.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

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

    private static final String ACCOUNT_SID = "AC017d20421440805fb53e3ab4b8bb88a3";
    private static final String AUTH_TOKEN = "bdc71685db25336c5e461c1baec73b6a";
    private static final String FROM_PHONE_NUMBER = "+17085894572";

//    public PractiseSession addPractiseSession(PractiseSession practiseSession) {
//        return practiseSessionRepository.save(practiseSession);
//    }

    public PractiseSession addPractiseSession(PractiseSession practiseSession) {
        // Fetch the team based on the teamId from the practiseSession
        Team team = teamRepository.findById(practiseSession.getTeam().getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Set the team for the practise session
        practiseSession.setTeam(team);

        PractiseSession savedPractiseSession = practiseSessionRepository.save(practiseSession);

        // Send SMS notifications
        sendSmsNotifications(team, savedPractiseSession);

        return savedPractiseSession;
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
        List<PracticeSessionResponse> practiceSessionResponses = new ArrayList<>();
        for (PractiseSession practiceSession : practiceSessions) {
            PracticeSessionResponse practiceSessionResponse = refactorResponse(practiceSession);
            practiceSessionResponses.add(practiceSessionResponse);
        }
        return practiceSessionResponses;
    }

    public PracticeSessionResponse updatePractiseSession(Long pracId, PractiseSession practiseSessionDetails) {
        // Fetch the existing PractiseSession by ID
        PractiseSession existingPractiseSession = practiseSessionRepository.findById(pracId)
                .orElseThrow(() -> new RuntimeException("PractiseSession not found with ID: " + pracId));

        // Update the fields
        existingPractiseSession.setVenue(practiseSessionDetails.getVenue());
        existingPractiseSession.setDate(practiseSessionDetails.getDate());
        existingPractiseSession.setStartTime(practiseSessionDetails.getStartTime());
        existingPractiseSession.setEndTime(practiseSessionDetails.getEndTime());
        existingPractiseSession.setPracType(practiseSessionDetails.getPracType());
        existingPractiseSession.setUpdatedBy(practiseSessionDetails.getUpdatedBy());
        existingPractiseSession.setUpdatedOn(practiseSessionDetails.getUpdatedOn());

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
        response.setStartTime(practiseSession.getStartTime());
        response.setEndTime(practiseSession.getEndTime());
        response.setPracType(practiseSession.getPracType());
        response.setTeamUnder(practiseSession.getTeam().getUnder());
        response.setTeamYear(practiseSession.getTeam().getYear());
        response.setTeamId(practiseSession.getTeam().getTeamId());
        response.setCreatedBy(practiseSession.getCreatedBy());
        response.setUpdatedBy(practiseSession.getUpdatedBy());
        response.setCreatedOn(practiseSession.getCreatedOn());
        response.setUpdatedOn(practiseSession.getUpdatedOn());
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
            practiceSessionResponse.setStartTime(practiceSession.getStartTime());
            practiceSessionResponse.setEndTime(practiceSession.getEndTime());
            practiceSessionResponse.setPracType(practiceSession.getPracType());
            practiceSessionResponse.setTeamUnder(practiceSession.getTeam().getUnder());
            practiceSessionResponseList.add(practiceSessionResponse);

        }
        return practiceSessionResponseList;
    }


    private void sendSmsNotifications(Team team, PractiseSession practiseSession) {
        // Initialize Twilio
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // Construct the message
        String messageBody = "New Practice Session Scheduled: "
                + "\nDate: " + practiseSession.getDate()
                + "\nTime: " + practiseSession.getStartTime()
                + "\nTime: " + practiseSession.getEndTime()
                + "\nVenue: " + practiseSession.getVenue();

        // Send SMS to each player in the team
        team.getPlayers().forEach(player -> {
            String playerContact = player.getContactNo();
            if (playerContact != null && !playerContact.isEmpty()) {
                sendSms(playerContact, messageBody);
            }
        });
    }

    private void sendSms(String toPhoneNumber, String messageBody) {
        Message.creator(
                new com.twilio.type.PhoneNumber(toPhoneNumber),
                new com.twilio.type.PhoneNumber(FROM_PHONE_NUMBER),
                messageBody
        ).create();
    }
}

