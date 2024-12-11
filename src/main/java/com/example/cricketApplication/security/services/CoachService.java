package com.example.cricketApplication.security.services;

import com.example.cricketApplication.exceptions.PlayerAlreadyExistsException;
import com.example.cricketApplication.models.Coach;
import com.example.cricketApplication.models.Team;
import com.example.cricketApplication.models.User;
import com.example.cricketApplication.payload.response.CoachResponse;
import com.example.cricketApplication.payload.response.TeamResponse;
import com.example.cricketApplication.repository.CoachRepository;
import com.example.cricketApplication.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CoachService {

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String IMAGE_DIRECTORY = "D:\\upload\\";

    public Coach addCoach(Coach coach) {
        return coachRepository.save(coach);
    }

    public CoachResponse getCoachById(Long coachId) {
        Optional<Coach> coach =  coachRepository.findById(coachId);
        return RefactorResponse(coach);
    }


    private CoachResponse RefactorResponse(Optional<Coach> coachOp) {
        Coach coach = coachOp.get();
        CoachResponse coachResponse = new CoachResponse();
        coachResponse.setCoachId(coach.getCoachId());
        coachResponse.setName(coach.getName());
        coachResponse.setEmail(coach.getEmail());
        coachResponse.setContactNo(coach.getContactNo());
        coachResponse.setAddress(coach.getAddress());
        coachResponse.setDateOfBirth(coach.getDateOfBirth());
        coachResponse.setImage(coach.getImage());
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(coach.getImage())
                .toUriString();
        coachResponse.setImage(imageUrl);
        coachResponse.setDescription(coach.getDescription());
        coachResponse.setUsername(coach.getUser().getUsername());
        coachResponse.setPassword(coach.getUser().getPassword());
        coachResponse.setStatus(coach.getStatus());
        coachResponse.setCreatedBy(coach.getCreatedBy());
        coachResponse.setUpdatedBy(coach.getUpdatedBy());
        coachResponse.setCreatedOn(coach.getCreatedOn());
        coachResponse.setUpdatedOn(coach.getUpdatedOn());


        // Handle the case where the User is null
        if (coach.getUser() != null) {
            coachResponse.setUsername(coach.getUser().getUsername());
            coachResponse.setEmail(coach.getUser().getEmail());
            coachResponse.setPassword(coach.getUser().getPassword());
        } else {
            coachResponse.setUsername(null); // Or set a default value
            coachResponse.setEmail(null); // Or set a default value
        }
        return coachResponse;
    }

//    public List<Coach> getAllCoaches() {
//        return coachRepository.findAll();
//    }

    public List<CoachResponse> getAllCoaches() {
        List<Coach> coach = coachRepository.findAll();
        return RefactorResponse(coach);  // Convert to MatchResponse list
    }



    public Optional<Coach> getCoachByUserId(Long userId) {

        return coachRepository.findByUser_Id(userId);
    }

    public List<Coach> getCoachesByRoleId(Long roleId) {
        return coachRepository.findByRole_Id(roleId);
    }

    public void deleteCoachById(Long coachId) {
        coachRepository.deleteById(coachId);
    }


//    @Transactional
//    public CoachResponse updateCoach(Long coachId, Coach coachDetails) throws EntityNotFoundException {
//        // Fetch the coach by ID, throw an exception if not found
//        Coach coach = coachRepository.findById(coachId)
//                .orElseThrow(() -> new EntityNotFoundException("Coach not found with ID: " + coachId));
//
//        // Fetch the associated user from the coach
//        User user = coach.getUser();
//
//        // Check if the email is already taken by another user
//        if (userRepository.existsByEmail(coachDetails.getUser().getEmail())
//                && !user.getEmail().equals(coachDetails.getUser().getEmail())) {
//            throw new PlayerAlreadyExistsException("Coach with email " + coachDetails.getUser().getEmail() + " already exists.");
//        }
//
//        // Check if the username is already taken by another user
//        if (userRepository.existsByUsername(coachDetails.getUser().getUsername())
//                && !user.getUsername().equals(coachDetails.getUser().getUsername())) {
//            throw new PlayerAlreadyExistsException("Coach with username " + coachDetails.getUser().getUsername() + " already exists.");
//        }
//
//
//        // Ensure the coachDetails contains user information before updating
//        if (coachDetails.getUser() != null) {
//            // Update the username if provided in the request body
//            if (coachDetails.getUser().getUsername() != null && !coachDetails.getUser().getUsername().isEmpty()) {
//                user.setUsername(coachDetails.getUser().getUsername());
//            }
//
//            // Update the email if provided in the request body
//            if (coachDetails.getUser().getEmail() != null && !coachDetails.getUser().getEmail().isEmpty()) {
//                user.setEmail(coachDetails.getUser().getEmail());
//            }
//
//            // Update the password if provided in the request body and encode it
//            if (coachDetails.getUser().getPassword() != null && !coachDetails.getUser().getPassword().isEmpty()) {
//                String encodedPassword = passwordEncoder.encode(coachDetails.getUser().getPassword());
//                user.setPassword(encodedPassword);
//            }
//            // Save the updated user
//            userRepository.save(user);
//        }
//
//        // Update the other coach details
//        coach.setImage(coachDetails.getImage());
//        coach.setDateOfBirth(coachDetails.getDateOfBirth());
//        coach.setName(coachDetails.getName());
//        coach.setAddress(coachDetails.getAddress());
//        coach.setDescription(coachDetails.getDescription());
//        coach.setContactNo(coachDetails.getContactNo());
//        coach.setEmail(coachDetails.getUser().getEmail());
//        coach.setStatus(coachDetails.getStatus());
//        coach.setUpdatedOn(coachDetails.getUpdatedOn());
//        coach.setUpdatedBy(coachDetails.getUpdatedBy());
//
//        // Save the updated coach
//        Coach updatedCoach = coachRepository.save(coach);
//
//        // Return the updated response using the refactorResponse method
//        return RefactorResponse(Collections.singletonList(updatedCoach)).get(0);
//    }



    private List<CoachResponse> RefactorResponse(List<Coach> coaches) {
        List<CoachResponse> coachResponses = new ArrayList<>();
        for (Coach coach : coaches) {
            CoachResponse coachResponse = new CoachResponse();
            coachResponse.setCoachId(coach.getCoachId());
            coachResponse.setName(coach.getName());
            coachResponse.setEmail(coach.getEmail());
            coachResponse.setContactNo(coach.getContactNo());
            coachResponse.setAddress(coach.getAddress());
            coachResponse.setDateOfBirth(coach.getDateOfBirth());
            coachResponse.setImage(coach.getImage());
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/images/")
                    .path(coach.getImage())
                    .toUriString();
            coachResponse.setImage(imageUrl);

            coachResponse.setDescription(coach.getDescription());
            coachResponse.setUsername(coach.getUser().getUsername());
            coachResponse.setPassword(coach.getUser().getPassword());
            coachResponse.setStatus(coach.getStatus());
            coachResponse.setCreatedBy(coach.getCreatedBy());
            coachResponse.setUpdatedBy(coach.getUpdatedBy());
            coachResponse.setCreatedOn(coach.getCreatedOn());
            coachResponse.setUpdatedOn(coach.getUpdatedOn());

            // Handle the case where the User is null
            if (coach.getUser() != null) {
                coachResponse.setUsername(coach.getUser().getUsername());
                coachResponse.setEmail(coach.getUser().getEmail());
            } else {
                coachResponse.setUsername(null); // Or set a default value
                coachResponse.setEmail(null); // Or set a default value
            }

            coachResponses.add(coachResponse);
        }
        return coachResponses;
    }

    @Transactional
    public CoachResponse updateCoach(Long coachId, Coach coachDetails, MultipartFile imageFile) {
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with ID: " + coachId));

        User user = coach.getUser();

        // Check if the email is already taken by another user
        if (userRepository.existsByEmail(coachDetails.getUser().getEmail())
                && !user.getEmail().equals(coachDetails.getUser().getEmail())) {
            throw new PlayerAlreadyExistsException("Coach with email " + coachDetails.getUser().getEmail() + " already exists.");
        }

        // Check if the username is already taken by another user
        if (userRepository.existsByUsername(coachDetails.getUser().getUsername())
                && !user.getUsername().equals(coachDetails.getUser().getUsername())) {
            throw new PlayerAlreadyExistsException("Coach with username " + coachDetails.getUser().getUsername() + " already exists.");
        }

        // Update user details if present
        if (coachDetails.getUser() != null) {
            if (coachDetails.getUser().getUsername() != null && !coachDetails.getUser().getUsername().isEmpty()) {
                user.setUsername(coachDetails.getUser().getUsername());
            }
            if (coachDetails.getUser().getEmail() != null && !coachDetails.getUser().getEmail().isEmpty()) {
                user.setEmail(coachDetails.getUser().getEmail());
            }
            if (coachDetails.getUser().getPassword() != null && !coachDetails.getUser().getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(coachDetails.getUser().getPassword());
                user.setPassword(encodedPassword);
            }
            userRepository.save(user);
        }

        // Update coach details
        coach.setName(coachDetails.getName());
        coach.setDateOfBirth(coachDetails.getDateOfBirth());
        coach.setAddress(coachDetails.getAddress());
        coach.setDescription(coachDetails.getDescription());
        coach.setContactNo(coachDetails.getContactNo());
        coach.setEmail(coachDetails.getUser().getEmail());
        coach.setStatus(coachDetails.getStatus());
        coach.setUpdatedOn(coachDetails.getUpdatedOn());
        coach.setUpdatedBy(coachDetails.getUpdatedBy());

        // Update image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = coachDetails.getUser().getUsername() + ".jpg"; // Use coach's username or other identifier
                String imagePath = IMAGE_DIRECTORY + fileName;

                // Save the image file locally
                Files.write(Paths.get(imagePath), imageFile.getBytes());

                // Update coach image path
                coach.setImage(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Error saving image file: " + e.getMessage(), e);
            }
        }

        // Save the updated coach
        Coach updatedCoach = coachRepository.save(coach);

        return refactorResponse(updatedCoach);
    }

    private CoachResponse refactorResponse(Coach coach) {
        CoachResponse coachResponse = new CoachResponse();
        coachResponse.setCoachId(coach.getCoachId());
        coachResponse.setName(coach.getName());
        coachResponse.setEmail(coach.getEmail());
        coachResponse.setContactNo(coach.getContactNo());
        coachResponse.setAddress(coach.getAddress());
        coachResponse.setDateOfBirth(coach.getDateOfBirth());
        coachResponse.setImage(coach.getImage());
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(coach.getImage())
                .toUriString();
        coachResponse.setImage(imageUrl);

        coachResponse.setDescription(coach.getDescription());
        coachResponse.setUsername(coach.getUser().getUsername());
        coachResponse.setPassword(coach.getUser().getPassword());
        coachResponse.setStatus(coach.getStatus());
        coachResponse.setCreatedBy(coach.getCreatedBy());
        coachResponse.setUpdatedBy(coach.getUpdatedBy());
        coachResponse.setCreatedOn(coach.getCreatedOn());
        coachResponse.setUpdatedOn(coach.getUpdatedOn());

        return coachResponse;
    }


}

