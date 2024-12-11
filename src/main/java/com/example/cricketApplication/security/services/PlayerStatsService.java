package com.example.cricketApplication.security.services;

import com.example.cricketApplication.models.Match;
import com.example.cricketApplication.models.Player;
import com.example.cricketApplication.models.PlayerStats;
import com.example.cricketApplication.payload.response.MatchResponse;
import com.example.cricketApplication.payload.response.MatchResponseWithStat;
import com.example.cricketApplication.payload.response.PlayerResponse;
import com.example.cricketApplication.payload.response.PlayerStatsResponse;
import com.example.cricketApplication.repository.MatchRepository;
import com.example.cricketApplication.repository.PlayerRepository;
import com.example.cricketApplication.repository.PlayerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerStatsService {

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public PlayerStats createPlayerStats(PlayerStats playerStats) {
        return playerStatsRepository.save(playerStats);
    }

    public Optional<PlayerStats> getPlayerStatsById(Long id) {
        return playerStatsRepository.findById(id);
    }

    public List<PlayerStats> getAllPlayerStats() {
        return playerStatsRepository.findAll();
    }

    public List<PlayerStats> getPlayerStatsByPlayerId(Long playerId) {
        return playerStatsRepository.findByPlayer_PlayerId(playerId);
    }

    public List<PlayerStats> getPlayerStatsByMatchId(Long matchId) {
        return playerStatsRepository.findByMatch_MatchId(matchId);
    }

    public void deletePlayerStatsById(Long id) {
        playerStatsRepository.deleteById(id);
    }

//    public List<PlayerStats> getPlayerStatsByMatch(Long playerId, Long matchId) {
//        Player player = playerRepository.findById(playerId)
//                .orElseThrow();
//
//        Match match = matchRepository.findById(matchId)
//                .orElseThrow();
//
//        return playerStatsRepository.findByPlayerAndMatch(player, match);
//    }

    public List<PlayerStatsResponse> getPlayerStatsByMatch(Long playerId, Long matchId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow();
                        //-> new ResourceNotFoundException("Player not found with id " + playerId));
        Match match = matchRepository.findById(matchId)
                .orElseThrow() ;
        //-> new ResourceNotFoundException("Match not found with id " + matchId));

        List<PlayerStats> playerStatsList = playerStatsRepository.findByPlayerAndMatch(player, match);
        return RefactorResponse(playerStatsList);
    }


    public List<PlayerStatsResponse> getPlayerStatsByMatchType(Long playerId, String matchType) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow();
                        //-> new ResourceNotFoundException("Player not found with id " + playerId));

        List<PlayerStats> playerStatsList = playerStatsRepository.findByPlayerAndMatch_Type(player, matchType);
        return RefactorResponse(playerStatsList);
    }



    public List<PlayerStatsResponse> getPlayerStatsForMatch(Long matchId) {
        // Fetch the match by matchId
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with ID: " + matchId));

        // Fetch all PlayerStats associated with the match
        List<PlayerStats> playerStatsList = playerStatsRepository.findByMatch(match);

        // Use RefactorResponse to convert the list of PlayerStats to PlayerStatsResponse
        return RefactorResponse(playerStatsList);
    }


    // New method to get all player stats by player ID
    public List<PlayerStatsResponse> getAllStatsByPlayerId(Long playerId) {
        // Find the player by ID
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerId));

        // Fetch all PlayerStats for the player
        List<PlayerStats> playerStatsList = playerStatsRepository.findByPlayer_PlayerId(playerId);

        // Use the RefactorResponse method to convert the list of PlayerStats to PlayerStatsResponse
        return refactorResponse(playerStatsList);
    }


    public List<PlayerStats> createMultiplePlayerStats(List<PlayerStats> playerStatsList) {
        return playerStatsRepository.saveAll(playerStatsList);  // saveAll method is used to persist the list
    }


    public PlayerStats updatePlayerStats(Long id, PlayerStats updatedPlayerStats) {
        PlayerStats existingPlayerStats = playerStatsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PlayerStats not found with ID: " + id));

        // Updating fields
        existingPlayerStats.setRuns(updatedPlayerStats.getRuns());
        existingPlayerStats.setBalls(updatedPlayerStats.getBalls());
        existingPlayerStats.setOvers(updatedPlayerStats.getOvers());
        existingPlayerStats.setCenturies(updatedPlayerStats.getCenturies());
        existingPlayerStats.setFifties(updatedPlayerStats.getFifties());
        existingPlayerStats.setFours(updatedPlayerStats.getFours());
        existingPlayerStats.setSixers(updatedPlayerStats.getSixers());
        existingPlayerStats.setWickets(updatedPlayerStats.getWickets());
        existingPlayerStats.setRunsConceded(updatedPlayerStats.getRunsConceded());
        existingPlayerStats.setWides(updatedPlayerStats.getWides());
        existingPlayerStats.setNoBalls(updatedPlayerStats.getNoBalls());
        existingPlayerStats.setMaidens(updatedPlayerStats.getMaidens());
        existingPlayerStats.setInning(updatedPlayerStats.getInning());
        existingPlayerStats.setPlayer(updatedPlayerStats.getPlayer());
        existingPlayerStats.setMatch(updatedPlayerStats.getMatch());
        existingPlayerStats.setUpdatedBy(updatedPlayerStats.getUpdatedBy());
        existingPlayerStats.setUpdatedOn(updatedPlayerStats.getUpdatedOn());
        existingPlayerStats.setHowOut(updatedPlayerStats.getHowOut());
        existingPlayerStats.setCatches(updatedPlayerStats.getCatches());
        existingPlayerStats.setStumps(updatedPlayerStats.getStumps());
        existingPlayerStats.setRunOuts(updatedPlayerStats.getRunOuts());
        existingPlayerStats.setRemarks(updatedPlayerStats.getRemarks());

        return playerStatsRepository.save(existingPlayerStats);
    }

    private List<PlayerStatsResponse> RefactorResponse(List<PlayerStats> playerStatsList) {
        List<PlayerStatsResponse> playerStatsResponseList = new ArrayList<>();
        for (PlayerStats playerStats : playerStatsList) {
            PlayerStatsResponse playerStatsResponse = new PlayerStatsResponse();
            playerStatsResponse.setId(playerStats.getId());
            playerStatsResponse.setBalls(playerStats.getBalls());
            playerStatsResponse.setCenturies(playerStats.getCenturies());
            playerStatsResponse.setInning(playerStats.getInning());
            playerStatsResponse.setFifties(playerStats.getFifties());
            playerStatsResponse.setFours(playerStats.getFours());
            playerStatsResponse.setSixers(playerStats.getSixers());
            playerStatsResponse.setOvers(playerStats.getOvers());
            playerStatsResponse.setRuns(playerStats.getRuns());
            playerStatsResponse.setSixers(playerStats.getSixers());
            playerStatsResponse.setWickets(playerStats.getWickets());
            playerStatsResponse.setRunsConceded(playerStats.getRunsConceded());
            playerStatsResponse.setWides(playerStats.getWides());
            playerStatsResponse.setNoBalls(playerStats.getNoBalls());
            playerStatsResponse.setMaidens(playerStats.getMaidens());
            playerStatsResponse.setHowOut(playerStats.getHowOut());
            playerStatsResponse.setCreatedBy(playerStats.getCreatedBy());
            playerStatsResponse.setUpdatedBy(playerStats.getUpdatedBy());
            playerStatsResponse.setCreatedOn(playerStats.getCreatedOn());
            playerStatsResponse.setUpdatedOn(playerStats.getUpdatedOn());
            playerStatsResponse.setCatches(playerStats.getCatches());
            playerStatsResponse.setStumps(playerStats.getStumps());
            playerStatsResponse.setRunOuts(playerStats.getRunOuts());
            playerStatsResponse.setRemarks(playerStats.getRemarks());

            PlayerResponse playerResponse = new PlayerResponse();
            playerResponse.setPlayerId(playerStats.getPlayer().getPlayerId());
            playerResponse.setName(playerStats.getPlayer().getName());

            playerStatsResponse.setPlayer(playerResponse);

            MatchResponseWithStat matchResponseWithStat = new MatchResponseWithStat();
            if (playerStats.getMatch() != null) {
                matchResponseWithStat.setMatchId(String.valueOf(playerStats.getMatch().getMatchId()));
                matchResponseWithStat.setType(playerStats.getMatch().getType());
                //matchResponseWithStat.setUnder(playerStats.getMatch().getTeam().getUnder());
                //matchResponseWithStat.setYear(playerStats.getMatch().getTeam().getYear());
            }
            playerStatsResponse.setMatch(matchResponseWithStat);

//            MatchResponse matchResponse = new MatchResponse();
//            matchResponse.setMatchId(playerStats.getMatch().getMatchId());
//            matchResponse.setType(playerStats.getMatch().getType());


            playerStatsResponseList.add(playerStatsResponse);

        }
        return playerStatsResponseList;
    }

    private List<PlayerStatsResponse> refactorResponse(List<PlayerStats> playerStatsList) {
        List<PlayerStatsResponse> playerStatsResponseList = new ArrayList<>();
        for (PlayerStats playerStats : playerStatsList) {
            PlayerStatsResponse playerStatsResponse = new PlayerStatsResponse();
            playerStatsResponse.setId(playerStats.getId());
            playerStatsResponse.setBalls(playerStats.getBalls());
            playerStatsResponse.setCenturies(playerStats.getCenturies());
            playerStatsResponse.setInning(playerStats.getInning());
            playerStatsResponse.setFifties(playerStats.getFifties());
            playerStatsResponse.setFours(playerStats.getFours());
            playerStatsResponse.setSixers(playerStats.getSixers());
            playerStatsResponse.setOvers(playerStats.getOvers());
            playerStatsResponse.setRuns(playerStats.getRuns());
            playerStatsResponse.setSixers(playerStats.getSixers());
            playerStatsResponse.setWickets(playerStats.getWickets());
            playerStatsResponse.setRunsConceded(playerStats.getRunsConceded());
            playerStatsResponse.setWides(playerStats.getWides());
            playerStatsResponse.setNoBalls(playerStats.getNoBalls());
            playerStatsResponse.setMaidens(playerStats.getMaidens());
            playerStatsResponse.setHowOut(playerStats.getHowOut());
            playerStatsResponse.setCreatedBy(playerStats.getCreatedBy());
            playerStatsResponse.setUpdatedBy(playerStats.getUpdatedBy());
            playerStatsResponse.setCreatedOn(playerStats.getCreatedOn());
            playerStatsResponse.setUpdatedOn(playerStats.getUpdatedOn());
            playerStatsResponse.setCatches(playerStats.getCatches());
            playerStatsResponse.setStumps(playerStats.getStumps());
            playerStatsResponse.setRunOuts(playerStats.getRunOuts());
            playerStatsResponse.setRemarks(playerStats.getRemarks());

            PlayerResponse playerResponse = new PlayerResponse();
            playerResponse.setPlayerId(playerStats.getPlayer().getPlayerId());
            playerResponse.setName(playerStats.getPlayer().getName());

            playerStatsResponse.setPlayer(playerResponse);

            MatchResponseWithStat matchResponseWithStat = new MatchResponseWithStat();
            if (playerStats.getMatch() != null) {
                matchResponseWithStat.setMatchId(String.valueOf(playerStats.getMatch().getMatchId()));
                matchResponseWithStat.setType(playerStats.getMatch().getType());
                matchResponseWithStat.setUnder(playerStats.getMatch().getTeam().getUnder());
                matchResponseWithStat.setYear(playerStats.getMatch().getTeam().getYear());
            }
            playerStatsResponse.setMatch(matchResponseWithStat);

//            MatchResponse matchResponse = new MatchResponse();
//            matchResponse.setMatchId(playerStats.getMatch().getMatchId());
//            matchResponse.setType(playerStats.getMatch().getType());


            playerStatsResponseList.add(playerStatsResponse);

        }
        return playerStatsResponseList;
    }

}
