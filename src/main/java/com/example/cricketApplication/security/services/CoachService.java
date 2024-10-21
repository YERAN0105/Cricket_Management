package com.example.cricketApplication.security.services;

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
        coachResponse.setDescription(coach.getDescription());
        coachResponse.setUsername(coach.getUser().getUsername());
        coachResponse.setPassword(coach.getUser().getPassword());


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


    @Transactional
    public CoachResponse updateCoach(Long coachId, Coach coachDetails) throws EntityNotFoundException {
        // Fetch the coach by ID, throw an exception if not found
        Coach coach = coachRepository.findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found with ID: " + coachId));

        // Fetch the associated user from the coach
        User user = coach.getUser();

        // Ensure the coachDetails contains user information before updating
        if (coachDetails.getUser() != null) {
            // Update the username if provided in the request body
            if (coachDetails.getUser().getUsername() != null && !coachDetails.getUser().getUsername().isEmpty()) {
                user.setUsername(coachDetails.getUser().getUsername());
            }

            // Update the email if provided in the request body
            if (coachDetails.getUser().getEmail() != null && !coachDetails.getUser().getEmail().isEmpty()) {
                user.setEmail(coachDetails.getUser().getEmail());
            }

            // Update the password if provided in the request body and encode it
            if (coachDetails.getUser().getPassword() != null && !coachDetails.getUser().getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(coachDetails.getUser().getPassword());
                user.setPassword(encodedPassword);
            }

            // Save the updated user
            userRepository.save(user);
        }

        // Update the other coach details
        coach.setImage(coachDetails.getImage());
        coach.setDateOfBirth(coachDetails.getDateOfBirth());
        coach.setName(coachDetails.getName());
        coach.setAddress(coachDetails.getAddress());
        coach.setDescription(coachDetails.getDescription());
        coach.setContactNo(coachDetails.getContactNo());
        coach.setEmail(coachDetails.getUser().getEmail());

        // Save the updated coach
        Coach updatedCoach = coachRepository.save(coach);

        // Return the updated response using the refactorResponse method
        return RefactorResponse(Collections.singletonList(updatedCoach)).get(0);
    }



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
            coachResponse.setDescription(coach.getDescription());
            coachResponse.setUsername(coach.getUser().getUsername());
            coachResponse.setPassword(coach.getUser().getPassword());

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
}

