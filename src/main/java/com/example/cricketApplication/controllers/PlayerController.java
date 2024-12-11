package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.Player;
import com.example.cricketApplication.payload.response.MessageResponse;
import com.example.cricketApplication.payload.response.PlayerResponse;
import com.example.cricketApplication.security.services.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;


    @PostMapping("/add")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Player> addPlayer(@RequestBody Player player) {
        Player savedPlayer = playerService.savePlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlayer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable Long id) {
        PlayerResponse playerResponse = playerService.getPlayerResponseById(id);
        return ResponseEntity.ok(playerResponse);
    }

    //    @GetMapping("/all")
//    public ResponseEntity<List<Player>> getAllPlayers() {
//        List<Player> players = playerService.getAllPlayers();
//        return ResponseEntity.ok(players);
//    }
    @GetMapping("/all")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PlayerResponse>> getAllPlayers() {
        List<PlayerResponse> players = playerService.getAllPlayerResponses();
        return ResponseEntity.ok(players);
    }

//    @PutMapping("/update/{id}")
//    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long id, @RequestBody Player playerDetails) {
//        PlayerResponse updatedPlayer = playerService.updatePlayer(id, playerDetails);
//        return ResponseEntity.ok(updatedPlayer);
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PlayerResponse> updatePlayer(
            @PathVariable Long id,
            @RequestParam("userData") String userData, // The player details as JSON
            @RequestParam(value = "image", required = false) MultipartFile imageFile) { // The image (optional)
        try {
            // Parse the JSON data into the Player object
            ObjectMapper objectMapper = new ObjectMapper();
            Player playerDetails = objectMapper.readValue(userData, Player.class);

            // Call the service to update the player with the image (if provided)
            PlayerResponse updatedPlayer = playerService.updatePlayer(id, playerDetails, imageFile);

            return ResponseEntity.ok(updatedPlayer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PlayerResponse("Error: " + e.getMessage()));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

//    @RequestMapping("/upload")
//    public class FileUploadController {
//
//        private static final String UPLOAD_DIR = "C:/inetpub/wwwroot/upload";
//
//        @PostMapping
//        public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
//            try {
//                // Ensure the upload directory exists
//                File uploadDir = new File(UPLOAD_DIR);
//                if (!uploadDir.exists()) {
//                    uploadDir.mkdirs(); // Create directories if they don't exist
//                }
//
//                // Save the file to the upload directory
//                String fileName = file.getOriginalFilename();
//                Path filePath = Paths.get(UPLOAD_DIR, fileName);
//                Files.write(filePath, file.getBytes());
//
//                // Construct the public URL for the uploaded file
//                String fileUrl = "http://rcc.dockyardsoftware.com/upload/" + fileName;
//
//                // Return the file URL as a response
//                return ResponseEntity.ok().body("{\"fileUrl\": \"" + fileUrl + "\"}");
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body("File upload failed: " + e.getMessage());
//            }
//        }
//    }
}


