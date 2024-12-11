package com.example.cricketApplication.security.services;

import com.example.cricketApplication.exceptions.PlayerAlreadyExistsException;
import com.example.cricketApplication.exceptions.PlayerNotFoundException;
import com.example.cricketApplication.models.Player;
import com.example.cricketApplication.models.PlayerStats;
import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.models.User;
import com.example.cricketApplication.payload.response.PlayerResponse;
import com.example.cricketApplication.payload.response.PlayerResponseWithTeamDetails;
import com.example.cricketApplication.payload.response.PlayerStatsResponse;
import com.example.cricketApplication.repository.MembershipRepository;
import com.example.cricketApplication.repository.PlayerRepository;
import com.example.cricketApplication.repository.UserRepository;
import com.example.cricketApplication.security.WebConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;




@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private HttpServletRequest request;

    private static final String IMAGE_DIRECTORY = "D:\\upload\\";

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

    public PlayerResponse updatePlayer(Long id, Player playerDetails, MultipartFile imageFile) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + id));

        User user = player.getUser();

        // Check if the email is already taken by another user
        if (userRepository.existsByEmail(playerDetails.getUser().getEmail())
                && !user.getEmail().equals(playerDetails.getUser().getEmail())) {
            throw new PlayerAlreadyExistsException("Player with email " + playerDetails.getUser().getEmail() + " already exists.");
        }

        // Check if the username is already taken by another user
        if (userRepository.existsByUsername(playerDetails.getUser().getUsername())
                && !user.getUsername().equals(playerDetails.getUser().getUsername())) {
            throw new PlayerAlreadyExistsException("Plyer with username " + playerDetails.getUser().getUsername() + " already exists.");
        }

        // Ensure the coachDetails contains user information before updating
        if (playerDetails.getUser() != null) {
            // Update the username if provided in the request body
            if (playerDetails.getUser().getUsername() != null && !playerDetails.getUser().getUsername().isEmpty()) {
                user.setUsername(playerDetails.getUser().getUsername());
            }

            // Update the email if provided in the request body
            if (playerDetails.getUser().getEmail() != null && !playerDetails.getUser().getEmail().isEmpty()) {
                user.setEmail(playerDetails.getUser().getEmail());
            }

            // Update the password if provided in the request body and encode it
            if (playerDetails.getUser().getPassword() != null && !playerDetails.getUser().getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(playerDetails.getUser().getPassword());
                user.setPassword(encodedPassword);
            }

            // Save the updated user
            userRepository.save(user);
        }
        // Update player details
        player.setName(playerDetails.getName());
        player.setDateOfBirth(playerDetails.getDateOfBirth());
        player.setEmail(playerDetails.getUser().getEmail());
        player.setContactNo(playerDetails.getContactNo());
        player.setBattingStyle(playerDetails.getBattingStyle());
        player.setBowlingStyle(playerDetails.getBowlingStyle());
        player.setStatus(playerDetails.getStatus());
        //player.setImage(playerDetails.getImage());
        player.setPlayerRole(playerDetails.getPlayerRole());
        player.setMembership(playerDetails.getMembership());
        player.setUpdatedOn(playerDetails.getUpdatedOn());
        player.setUpdatedBy(playerDetails.getUpdatedBy());
        //player.setRole(playerDetails.getRole());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = playerDetails.getUser().getUsername() + ".jpg"; // Use the player's username or another identifier
                String imagePath = IMAGE_DIRECTORY + fileName;

                // Save the image file locally
                Files.write(Paths.get(imagePath), imageFile.getBytes());

                // Update player image path
                player.setImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error saving image file: " + e.getMessage(), e);
            }
        }

        membershipRepository.save(playerDetails.getMembership());

        Player updatedPlayer = playerRepository.save(player);

        return refactorResponse(updatedPlayer);
    }

    public void deletePlayer(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found with ID: " + id));
        playerRepository.delete(player);
        userRepository.delete(player.getUser());
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
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(player.getImage())
                .toUriString();
        playerResponse.setImage(imageUrl);
        //playerResponse.setImage("http://rcc.dockyardsoftware.com/upload/"+ player.getImage());

//          String serverName = request.getServerName(); // Get the server name dynamically (localhost or production)
//          String imageUrl = "http://" + serverName + "/images/" + player.getImage();
//          playerResponse.setImage(imageUrl);



        playerResponse.setPlayerRole(player.getPlayerRole());
        playerResponse.setStartDate(String.valueOf(player.getMembership().getStartDate()));
        playerResponse.setEndDate(String.valueOf(player.getMembership().getEndDate()));
        playerResponse.setPassword(player.getUser().getPassword());
        playerResponse.setUsername(player.getUser().getUsername());
        playerResponse.setMembershipId(player.getMembership().getId());
        playerResponse.setMembershipStartDate(player.getMembership().getStartDate());
        playerResponse.setMembershipEndDate(player.getMembership().getEndDate());
        playerResponse.setCreatedBy(player.getCreatedBy());
        playerResponse.setUpdatedBy(player.getUpdatedBy());
        playerResponse.setCreatedOn(player.getCreatedOn());
        playerResponse.setUpdatedOn(player.getUpdatedOn());

        // Populate team details using PlayerResponseWithTeamDetails
        List<PlayerResponseWithTeamDetails> teamDetails = player.getTeams().stream()
                .map(team -> new PlayerResponseWithTeamDetails(team.getUnder(), String.valueOf(team.getYear())))
                .collect(Collectors.toList());
        playerResponse.setTeamDetails(teamDetails);

        return playerResponse;
    }
}


