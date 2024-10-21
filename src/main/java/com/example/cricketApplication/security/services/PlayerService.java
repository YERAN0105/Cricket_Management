package com.example.cricketApplication.security.services;

import com.example.cricketApplication.exceptions.PlayerAlreadyExistsException;
import com.example.cricketApplication.exceptions.PlayerNotFoundException;
import com.example.cricketApplication.models.Player;
import com.example.cricketApplication.models.PlayerStats;
import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.payload.response.PlayerResponse;
import com.example.cricketApplication.payload.response.PlayerStatsResponse;
import com.example.cricketApplication.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;




@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player savePlayer(Player player) {
        if (playerRepository.existsByEmail(player.getEmail())) {
            throw new PlayerAlreadyExistsException("Player with email " + player.getEmail() + " already exists.");
        }

        // You can add more validation logic here and throw InvalidPlayerDataException
        return playerRepository.save(player);
    }

    public PlayerResponse getPlayerResponseById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + id));
        return refactorResponse(player);
    }

//    public List<Player> getAllPlayers() {
//        return playerRepository.findAll();
//    }
    public List<PlayerResponse> getAllPlayerResponses() {
    List<Player> players = playerRepository.findAll();
    return players.stream()
            .map(this::refactorResponse)  // Convert each Player to PlayerResponse
            .collect(Collectors.toList());
    }

    public PlayerResponse updatePlayer(Long id, Player playerDetails) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + id));

        // Update player details
        player.setName(playerDetails.getName());
        player.setDateOfBirth(playerDetails.getDateOfBirth());
        player.setEmail(playerDetails.getEmail());
        player.setContactNo(playerDetails.getContactNo());
        player.setBattingStyle(playerDetails.getBattingStyle());
        player.setBowlingStyle(playerDetails.getBowlingStyle());
        player.setStatus(playerDetails.getStatus());
        player.setImage(playerDetails.getImage());
        player.setPlayerRole(playerDetails.getPlayerRole());

        Player updatedPlayer = playerRepository.save(player);

        return refactorResponse(updatedPlayer);
    }

    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + id));
        playerRepository.delete(player);
    }

    private PlayerResponse refactorResponse(Player player) {
        PlayerResponse playerResponse = new PlayerResponse();
        playerResponse.setPlayerId(player.getPlayerId());
        playerResponse.setName(player.getName());
        playerResponse.setDateOfBirth(player.getDateOfBirth());
        playerResponse.setEmail(player.getEmail());
        playerResponse.setContactNo(player.getContactNo());
        playerResponse.setBattingStyle(player.getBattingStyle());
        playerResponse.setBowlingStyle(player.getBowlingStyle());
        playerResponse.setStatus(player.getStatus());
        playerResponse.setImage(player.getImage());
        playerResponse.setPlayerRole(player.getPlayerRole());
        playerResponse.setStartDate(String.valueOf(player.getMembership().getStartDate()));
        playerResponse.setEndDate(String.valueOf(player.getMembership().getEndDate()));
        playerResponse.setPassword(player.getUser().getPassword());
        playerResponse.setUsername(player.getUser().getUsername());

        // Extract the 'under' values from the teams associated with the player
        List<String> teamUnders = player.getTeams().stream()
                .map(Team::getUnder)  // Get the 'under' value for each team
                .collect(Collectors.toList());
        playerResponse.setTeamsUnder(teamUnders);  // Set the list of 'under' values in the response

        return playerResponse;
    }
}


