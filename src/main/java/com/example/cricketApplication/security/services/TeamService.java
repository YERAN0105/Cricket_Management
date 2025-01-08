package com.example.cricketApplication.security.services;
import com.example.cricketApplication.models.Match;
import com.example.cricketApplication.models.Player;
import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.payload.response.MatchResponse;
import com.example.cricketApplication.payload.response.PlayerResponse;
import com.example.cricketApplication.payload.response.TeamResponse;
import com.example.cricketApplication.repository.PlayerRepository;
import com.example.cricketApplication.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

//    public Team addTeam(Team team) {
//        return teamRepository.save(team);
//    }

//    public Team addTeam(Team team) {
//        Optional<Team> existingTeam = teamRepository.findByUnderAndYear(team.getUnder(), team.getYear());
//
//        if (existingTeam.isPresent()) {
//            throw new IllegalArgumentException("A team with the same 'under' category and year already exists.");
//        }
//
//        return teamRepository.save(team);
//    }

    public Team addTeam(Team team) {
        // Validate captain
        if (team.getCaptain() != null) {
            playerRepository.findById(team.getCaptain().getPlayerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid captain ID: " + team.getCaptain().getPlayerId()));
        }

        // Validate vice-captain
        if (team.getViceCaptain() != null) {
            playerRepository.findById(team.getViceCaptain().getPlayerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid vice-captain ID: " + team.getViceCaptain().getPlayerId()));
        }

        // Save the team
        return teamRepository.save(team);
    }



    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);
    }

//    public List<TeamResponse> getAllTeams() {
//        return teamRepository.findAll();
//    }

    public List<TeamResponse> getAllTeams() {
       List<Team> team = teamRepository.findAll();
       return RefactorResponse(team);  // Convert to MatchResponse list
    }



    public void deleteTeamById(Long id) {
        teamRepository.deleteById(id);
    }


//    public Optional<Team> updateTeam(Long id, Team teamDetails) {
//        return teamRepository.findById(id).map(existingTeam -> {
//            // Update team details
//            existingTeam.setUnder(teamDetails.getUnder());
//            existingTeam.setYear(teamDetails.getYear());
//            existingTeam.setCaptain(teamDetails.getCaptain());
//            existingTeam.setPlayers(teamDetails.getPlayers());
//
//            // Save updated team
//            return teamRepository.save(existingTeam);
//        });
//    }

//    public Optional<TeamResponse> updateTeam(Long id, Team teamDetails) {
//        return teamRepository.findById(id).map(existingTeam -> {
//            // Update team details
//            existingTeam.setUnder(teamDetails.getUnder());
//            existingTeam.setYear(teamDetails.getYear());
//            existingTeam.setCaptain(teamDetails.getCaptain());
//            existingTeam.setViceCaptain(teamDetails.getViceCaptain());
//            existingTeam.setPlayers(teamDetails.getPlayers());
//            existingTeam.setUpdatedBy(teamDetails.getUpdatedBy());
//            existingTeam.setUpdatedOn(teamDetails.getUpdatedOn());
//
//            // Save updated team
//            Team updatedTeam = teamRepository.save(existingTeam);
//
//            // Convert updated team to TeamResponse
//            return RefactorResponse(updatedTeam);
//        });
//    }


    public Optional<TeamResponse> updateTeam(Long id, Team teamDetails) {
        // Validate captain if provided
        if (teamDetails.getCaptain() != null) {
            playerRepository.findById(teamDetails.getCaptain().getPlayerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid captain ID: "
                            + teamDetails.getCaptain().getPlayerId()));
        }

        // Validate vice-captain if provided
        if (teamDetails.getViceCaptain() != null) {
            playerRepository.findById(teamDetails.getViceCaptain().getPlayerId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid vice-captain ID: "
                            + teamDetails.getViceCaptain().getPlayerId()));
        }

        return teamRepository.findById(id).map(existingTeam -> {
            // Update team details
            existingTeam.setUnder(teamDetails.getUnder());
            existingTeam.setYear(teamDetails.getYear());
            existingTeam.setCaptain(teamDetails.getCaptain());
            existingTeam.setViceCaptain(teamDetails.getViceCaptain());
            existingTeam.setPlayers(teamDetails.getPlayers());
            existingTeam.setUpdatedBy(teamDetails.getUpdatedBy());
            existingTeam.setUpdatedOn(new Date());

            // Save updated team
            Team updatedTeam = teamRepository.save(existingTeam);

            // Convert updated team to TeamResponse
            return RefactorResponse(updatedTeam);
        });
    }




    public List<PlayerResponse> getPlayersByTeamId(Long teamId) {
        return teamRepository.findById(teamId)
                .map(team -> RefactorPlayerResponse(team.getPlayers()))
                .orElseThrow();
    }



    private List<TeamResponse> RefactorResponse(List<Team> team) {
        List<TeamResponse> teamResponses = new ArrayList<>();
        for (Team team1 : team) {
            TeamResponse teamResponse = new TeamResponse();
            teamResponse.setTeamId(team1.getTeamId());
            teamResponse.setYear(team1.getYear());
//            teamResponse.setCaptain(String.valueOf(team1.getCaptain()));
//            teamResponse.setViceCaptain(String.valueOf(team1.getViceCaptain()));

//            if (team1.getCaptain() != null) {
//                playerRepository.findById(team1.getCaptain().getPlayerId())
//                        .ifPresent(captain -> teamResponse.setCaptain(Long.valueOf(captain.getName())));
//            }
//
//            if (team1.getViceCaptain() != null) {
//                playerRepository.findById(team1.getViceCaptain().getPlayerId())
//                        .ifPresent(viceCaptain -> teamResponse.setViceCaptain(Long.valueOf(viceCaptain.getName())));
//            }

            if (team1.getCaptain() != null) {
                teamResponse.setCaptain(team1.getCaptain().getName());
            }

            // Set viceCaptain if exists
            if (team1.getViceCaptain() != null) {
                teamResponse.setViceCaptain(team1.getViceCaptain().getName());
            }

            teamResponse.setUnder(team1.getUnder());
            teamResponse.setCreatedBy(team1.getCreatedBy());
            teamResponse.setUpdatedBy(team1.getUpdatedBy());
            teamResponse.setCreatedOn(team1.getCreatedOn());
            teamResponse.setUpdatedOn(team1.getUpdatedOn());

            List<PlayerResponse> playerResponses = RefactorPlayerResponse(team1.getPlayers());
            teamResponse.setPlayers(playerResponses);

            teamResponses.add(teamResponse);
        }
        return teamResponses;
    }

    private TeamResponse RefactorResponse(Team team) {
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setTeamId(team.getTeamId());
        teamResponse.setYear(team.getYear());
//        teamResponse.setCaptain(String.valueOf(team.getCaptain()));
//        teamResponse.setViceCaptain(String.valueOf(team.getViceCaptain()));
//        if (team.getCaptain() != null) {
//            playerRepository.findById(team.getCaptain().getPlayerId())
//                    .ifPresent(captain -> teamResponse.setCaptain(Long.valueOf(captain.getName())));
//        }
//
//        if (team.getViceCaptain() != null) {
//            playerRepository.findById(team.getViceCaptain().getPlayerId())
//                    .ifPresent(viceCaptain -> teamResponse.setViceCaptain(Long.valueOf(viceCaptain.getName())));
//        }

        if (team.getCaptain() != null) {
            teamResponse.setCaptain(team.getCaptain().getName());
        }

        // Set viceCaptain if exists
        if (team.getViceCaptain() != null) {
            teamResponse.setViceCaptain(team.getViceCaptain().getName());
        }

        teamResponse.setUnder(team.getUnder());
        teamResponse.setCreatedBy(team.getCreatedBy());
        teamResponse.setUpdatedBy(team.getUpdatedBy());
        teamResponse.setCreatedOn(team.getCreatedOn());
        teamResponse.setUpdatedOn(team.getUpdatedOn());

        List<PlayerResponse> playerResponses = RefactorPlayerResponse(team.getPlayers());
        teamResponse.setPlayers(playerResponses);
        return teamResponse;
    }



    private List<PlayerResponse> RefactorPlayerResponse(Set<Player> players) {
        List<PlayerResponse> playerResponses = new ArrayList<>();
        for (Player player : players) {
            PlayerResponse playerResponse = new PlayerResponse();
            playerResponse.setPlayerId(player.getPlayerId());
            playerResponse.setName(player.getName());
            playerResponse.setEmail(player.getEmail());
            playerResponse.setContactNo(player.getContactNo());
            playerResponse.setBattingStyle(player.getBattingStyle());
            playerResponse.setBowlingStyle(player.getBowlingStyle());
            playerResponse.setPlayerRole(player.getPlayerRole());
            playerResponse.setImage(player.getImage());
            playerResponse.setCreatedBy(player.getCreatedBy());
            playerResponse.setUpdatedBy(player.getUpdatedBy());
            playerResponse.setCreatedOn(player.getCreatedOn());
            playerResponse.setUpdatedOn(player.getUpdatedOn());
            playerResponse.setStatus(player.getStatus());
            playerResponse.setDateOfBirth(player.getDateOfBirth());
            playerResponses.add(playerResponse);
        }
        return playerResponses;
    }




}

