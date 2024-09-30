package com.example.cricketApplication.security.services;
import com.example.cricketApplication.models.Match;
import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.payload.response.MatchResponse;
import com.example.cricketApplication.payload.response.TeamResponse;
import com.example.cricketApplication.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team addTeam(Team team) {
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

    public Optional<TeamResponse> updateTeam(Long id, Team teamDetails) {
        return teamRepository.findById(id).map(existingTeam -> {
            // Update team details
            existingTeam.setUnder(teamDetails.getUnder());
            existingTeam.setYear(teamDetails.getYear());
            existingTeam.setCaptain(teamDetails.getCaptain());
            existingTeam.setPlayers(teamDetails.getPlayers());

            // Save updated team
            Team updatedTeam = teamRepository.save(existingTeam);

            // Convert updated team to TeamResponse
            return RefactorResponse(updatedTeam);
        });
    }



    private List<TeamResponse> RefactorResponse(List<Team> team) {
        List<TeamResponse> teamResponses = new ArrayList<>();
        for (Team team1 : team) {
            TeamResponse teamResponse = new TeamResponse();
            teamResponse.setTeamId(team1.getTeamId());
            teamResponse.setYear(team1.getYear());
            teamResponse.setCaptain(team1.getCaptain());
            teamResponse.setUnder(team1.getUnder());

            teamResponses.add(teamResponse);
        }
        return teamResponses;
    }

    private TeamResponse RefactorResponse(Team team) {
        TeamResponse teamResponse = new TeamResponse();
        teamResponse.setTeamId(team.getTeamId());
        teamResponse.setYear(team.getYear());
        teamResponse.setCaptain(team.getCaptain());
        teamResponse.setUnder(team.getUnder());
        return teamResponse;
    }




}

