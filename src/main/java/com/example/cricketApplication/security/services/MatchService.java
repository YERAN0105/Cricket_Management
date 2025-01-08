package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.Coach;
import com.example.cricketApplication.models.Match;
import com.example.cricketApplication.models.Player;
import com.example.cricketApplication.payload.response.MatchCoachResponse;
import com.example.cricketApplication.payload.response.MatchResponse;
import com.example.cricketApplication.repository.MatchRepository;
import com.example.cricketApplication.repository.PlayerRepository;
import com.example.cricketApplication.security.WebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    private static final String LOGO_DIRECTORY = "D:\\upload\\";

    // Save a new match
//    public Match saveMatch(Match match) {
//        return matchRepository.save(match);
//    }


    // Save a new match
//    public Match saveMatch(Match match, MultipartFile logoFile) {
//        try {
//            // If logo is provided, save it to the file system
//            if (logoFile != null && !logoFile.isEmpty()) {
//                String fileName = match.getOpposition() + ".jpg"; // Use match ID or another unique identifier
//                String logoPath = LOGO_DIRECTORY + fileName;
//
//                // Save the logo file locally
//                Files.write(Paths.get(logoPath), logoFile.getBytes());
//
//                // Update the match with the logo filename
//                match.setLogo(fileName); // Or you can save the full path if preferred
//            }
//
//            // Save the match entity to the database
//            return matchRepository.save(match);
//        } catch (IOException e) {
//            throw new RuntimeException("Error saving logo file: " + e.getMessage(), e);
//        }
//    }



//    public Match saveMatch(Match match, MultipartFile logoFile) {
//        try {
//            // Validate matchCaptain
//            if (match.getMatchCaptain() != null) {
//                Player captain = playerRepository.findById(match.getMatchCaptain().getPlayerId())
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid match captain ID: " + match.getMatchCaptain().getPlayerId()));
//                match.setMatchCaptain(captain);
//            }
//
//            // Validate matchViceCaptain
//            if (match.getMatchViceCaptain() != null) {
//                Player viceCaptain = playerRepository.findById(match.getMatchViceCaptain().getPlayerId())
//                        .orElseThrow(() -> new IllegalArgumentException("Invalid match vice-captain ID: " + match.getMatchViceCaptain().getPlayerId()));
//                match.setMatchViceCaptain(viceCaptain);
//            }
//
//            // Handle the logo file
//            if (logoFile != null && !logoFile.isEmpty()) {
//                String fileName = match.getOpposition() + ".jpg";
//                String logoPath = LOGO_DIRECTORY + fileName;
//                Files.write(Paths.get(logoPath), logoFile.getBytes());
//                match.setLogo(fileName);
//            }
//
//            // Save the match entity to the database
//            return matchRepository.save(match);
//        } catch (IOException e) {
//            throw new RuntimeException("Error saving logo file: " + e.getMessage(), e);
//        }
//    }



    public List<MatchResponse> saveMatch(Match match, Long captainId, Long viceCaptainId, MultipartFile logoFile) {
        try {
            // Fetch the captain and vice-captain from the player repository
            Player captain = playerRepository.findById(captainId)
                    .orElseThrow(() -> new RuntimeException("Captain with ID " + captainId + " not found"));

            Player viceCaptain = playerRepository.findById(viceCaptainId)
                    .orElseThrow(() -> new RuntimeException("Vice-Captain with ID " + viceCaptainId + " not found"));

            // Set captain and vice-captain in the Match entity
            match.setMatchCaptain(captain);
            match.setMatchViceCaptain(viceCaptain);

            // If logo is provided, save it to the file system
            if (logoFile != null && !logoFile.isEmpty()) {
                String fileName = match.getOpposition().replaceAll("\\s+", "_") + ".jpg"; // Use opposition name for the file name
                String logoPath = LOGO_DIRECTORY + fileName;

                // Save the logo file locally
                Files.write(Paths.get(logoPath), logoFile.getBytes());

                // Update the match with the logo filename
                match.setLogo(fileName); // Or you can save the full path if preferred
            }

            // Save the match entity to the database
            Match savedMatch = matchRepository.save(match);

            // Call refactorResponse method to format the response
            return refactorResponse(List.of(savedMatch));

        } catch (IOException e) {
            throw new RuntimeException("Error saving logo file: " + e.getMessage(), e);
        }
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

    public boolean hasRequiredMatchSummaries(Long matchId) {
        // Retrieve match by id
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found"));

        int summaryCount = match.getMatchSummaries().size();

        switch (match.getType()) {
            case "Test":
                return summaryCount == 2;
            case "T20":
            case "ODI":
                return summaryCount == 1;
            default:
                throw new IllegalArgumentException("Unknown match type");
        }
    }


    // Delete a match by ID
    public void deleteMatch(Long matchId) {
        matchRepository.deleteById(matchId);
    }

//    public MatchResponse updateMatch(Long matchId, Match matchDetails, MultipartFile logoFile) throws IOException {
//        // Fetch the existing match by ID
//        Match match = matchRepository.findById(matchId)
//                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));
//
//        // Update the match details
//        match.setDate(matchDetails.getDate());
//        match.setVenue(matchDetails.getVenue());
//        match.setOpposition(matchDetails.getOpposition());
//        match.setTier(matchDetails.getTier());
//        match.setDivision(matchDetails.getDivision());
//        match.setType(matchDetails.getType());
//        match.setUmpires(matchDetails.getUmpires());
//        match.setMatchCaptain(matchDetails.getMatchCaptain());
//        match.setMatchViceCaptain(matchDetails.getMatchViceCaptain());
//        match.setTime(matchDetails.getTime());
//        match.setCoaches(matchDetails.getCoaches());
//        match.setTeam(matchDetails.getTeam());
//        match.setUpdatedBy(matchDetails.getUpdatedBy());
//        match.setUpdatedOn(matchDetails.getUpdatedOn());
//
//        // Handle the logo file
//        if (logoFile != null && !logoFile.isEmpty()) {
//            String fileName = match.getOpposition() + ".jpg";
//            String logoPath = LOGO_DIRECTORY + fileName;
//            Files.write(Paths.get(logoPath), logoFile.getBytes());
//            match.setLogo(fileName);
//        }
//
//        // Save the updated match
//        Match updatedMatch = matchRepository.save(match);
//
//        // Convert to MatchResponse and return
//        return refactorResponse(Collections.singletonList(updatedMatch)).get(0);
//    }


    public MatchResponse updateMatch(Long matchId, Match matchDetails, Long captainId, Long viceCaptainId, MultipartFile logoFile) {
        try {
            // Fetch the existing match by ID
            Match match = matchRepository.findById(matchId)
                    .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

            // Update the captain and vice-captain if IDs are provided
            if (captainId != null) {
                Player captain = playerRepository.findById(captainId)
                        .orElseThrow(() -> new RuntimeException("Captain with ID " + captainId + " not found"));
                match.setMatchCaptain(captain);
            }

            if (viceCaptainId != null) {
                Player viceCaptain = playerRepository.findById(viceCaptainId)
                        .orElseThrow(() -> new RuntimeException("Vice-Captain with ID " + viceCaptainId + " not found"));
                match.setMatchViceCaptain(viceCaptain);
            }

            // Update the match details
            match.setDate(matchDetails.getDate());
            match.setVenue(matchDetails.getVenue());
            match.setOpposition(matchDetails.getOpposition());
            match.setTier(matchDetails.getTier());
            match.setDivision(matchDetails.getDivision());
            match.setType(matchDetails.getType());
            match.setUmpires(matchDetails.getUmpires());
            match.setTime(matchDetails.getTime());
            match.setCoaches(matchDetails.getCoaches());
            match.setTeam(matchDetails.getTeam());
            match.setUpdatedBy(matchDetails.getUpdatedBy());
            match.setUpdatedOn(matchDetails.getUpdatedOn());

            // Handle the logo file
            if (logoFile != null && !logoFile.isEmpty()) {
                String fileName = match.getOpposition().replaceAll("\\s+", "_") + ".jpg"; // Use opposition name for the file name
                String logoPath = LOGO_DIRECTORY + fileName;

                // Save the new logo file locally
                Files.write(Paths.get(logoPath), logoFile.getBytes());

                // Update the match with the new logo filename
                match.setLogo(fileName);
            }

            // Save the updated match
            Match updatedMatch = matchRepository.save(match);

            // Convert to MatchResponse and return
            return refactorResponse(Collections.singletonList(updatedMatch)).get(0);
        } catch (IOException e) {
            throw new RuntimeException("Error saving logo file: " + e.getMessage(), e);
        }
    }


    private void saveFile(MultipartFile file, String fileName) {
        try {
            Path uploadPath = Paths.get("uploads/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(file.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }

    private List<MatchResponse> refactorResponse(List<Match> matchList) {
        return matchList.stream().map(match -> {
            MatchResponse matchResponse = new MatchResponse();
            matchResponse.setMatchId(match.getMatchId());
            matchResponse.setDate(match.getDate());
            matchResponse.setDivision(match.getDivision());
            matchResponse.setOpposition(match.getOpposition());
            matchResponse.setTier(match.getTier());
            matchResponse.setUmpires(match.getUmpires());
            matchResponse.setVenue(match.getVenue());
//            matchResponse.setMatchCaptain(match.getMatchCaptain());
//            matchResponse.setMatchViceCaptain(match.getMatchViceCaptain());
            matchResponse.setTime(match.getTime());
            matchResponse.setType(match.getType());
            matchResponse.setUnder(match.getTeam().getUnder());
            matchResponse.setTeamId(match.getTeam().getTeamId());
            matchResponse.setTeamYear(match.getTeam().getYear());

            // Set matchCaptain and matchViceCaptain details
//            if (match.getMatchCaptain() != null) {
//                matchResponse.setMatchCaptain(match.getMatchCaptain().getPlayerId());
//                matchResponse.setMatchCaptain(Long.valueOf(match.getMatchCaptain().getName()));
//            }
//            if (match.getMatchViceCaptain() != null) {
//                matchResponse.setMatchViceCaptain(Long.valueOf(String.valueOf(match.getMatchViceCaptain().getPlayerId())));
//                matchResponse.setMatchViceCaptain(Long.valueOf(match.getMatchViceCaptain().getName()));
//            }

            if (match.getMatchCaptain() != null){
                matchResponse.setMatchCaptain(match.getMatchCaptain().getName());
            }

            if (match.getMatchViceCaptain() != null){
                matchResponse.setMatchViceCaptain(match.getMatchViceCaptain().getName());
            }

//            matchResponse.setMatchCaptain(match.getMatchCaptain().getName());
//            matchResponse.setMatchViceCaptain(match.getMatchViceCaptain().getName());

            // Convert logo to full URL
            String logoUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(match.getLogo())
                    .toUriString();
            matchResponse.setLogo(logoUrl);

            matchResponse.setCreatedBy(match.getCreatedBy());
            matchResponse.setUpdatedBy(match.getUpdatedBy());
            matchResponse.setCreatedOn(match.getCreatedOn());
            matchResponse.setUpdatedOn(match.getUpdatedOn());

            // Refactor coaches
            List<MatchCoachResponse> coaches = match.getCoaches().stream()
                    .map(coach -> {
                        MatchCoachResponse coachResponse = new MatchCoachResponse();
                        coachResponse.setCoachId(coach.getCoachId());
                        coachResponse.setCoachName(coach.getName());
                        return coachResponse;
                    })
                    .collect(Collectors.toList());
            matchResponse.setCoaches(coaches);

            return matchResponse;
        }).collect(Collectors.toList());
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
//            matchResponse.setMatchCaptain(match.getMatchCaptain());
//            matchResponse.setMatchViceCaptain(match.getMatchViceCaptain());
            matchResponse.setTime(match.getTime());
            matchResponse.setType(match.getType());
            matchResponse.setUnder(match.getTeam().getUnder());
            matchResponse.setTeamId(match.getTeam().getTeamId());
            matchResponse.setTeamYear(match.getTeam().getYear());
            matchResponse.setLogo(match.getLogo());

            // Set matchCaptain and matchViceCaptain details
//            if (match.getMatchCaptain() != null) {
//                matchResponse.setMatchCaptain(Long.valueOf(String.valueOf(match.getMatchCaptain().getPlayerId())));
//                matchResponse.setMatchCaptain(Long.valueOf(match.getMatchCaptain().getName()));
//            }
//            if (match.getMatchViceCaptain() != null) {
//                matchResponse.setMatchViceCaptain(Long.valueOf(String.valueOf(match.getMatchViceCaptain().getPlayerId())));
//                matchResponse.setMatchViceCaptain(Long.valueOf(match.getMatchViceCaptain().getName()));
//            }

            if (match.getMatchCaptain() != null){
                matchResponse.setMatchCaptain(match.getMatchCaptain().getName());
            }

            if (match.getMatchViceCaptain() != null){
                matchResponse.setMatchViceCaptain(match.getMatchViceCaptain().getName());
            }
//            matchResponse.setMatchCaptain(match.getMatchCaptain().getName());
//            matchResponse.setMatchViceCaptain(match.getMatchViceCaptain().getName());

            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(match.getLogo())
                    .toUriString();
            matchResponse.setLogo(imageUrl);
            matchResponse.setCreatedBy(match.getCreatedBy());
            matchResponse.setUpdatedBy(match.getUpdatedBy());
            matchResponse.setCreatedOn(match.getCreatedOn());
            matchResponse.setUpdatedOn(match.getUpdatedOn());
            List<MatchCoachResponse> coaches = match.getCoaches().stream()
                    .map(coach -> {
                        MatchCoachResponse coachResponse = new MatchCoachResponse();
                        coachResponse.setCoachId(coach.getCoachId());
                        coachResponse.setCoachName(coach.getName());
                        return coachResponse;
                    })
                    .collect(Collectors.toList());

            matchResponse.setCoaches(coaches);
            matchResponseList.add(matchResponse);

        }
        return matchResponseList;
    }
}





