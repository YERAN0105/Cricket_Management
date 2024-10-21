package com.example.cricketApplication.security.services;

import com.example.cricketApplication.exceptions.OfficialNotFoundException;
import com.example.cricketApplication.models.Official;
import com.example.cricketApplication.models.User;
import com.example.cricketApplication.payload.response.OfficialResponse;
import com.example.cricketApplication.repository.OfficialRepository;
import com.example.cricketApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfficialService {

    @Autowired
    private OfficialRepository officialRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    // Get official by id method with OfficialResponse
    public OfficialResponse getOfficialResponseById(Long id) {
        Official official = officialRepository.findById(id)
                .orElseThrow(() -> new OfficialNotFoundException("Official not found with ID: " + id));
        return refactorResponse(official);
    }

    // Get all officials method with OfficialResponse
    public List<OfficialResponse> getAllOfficialResponses() {
        List<Official> officials = officialRepository.findAll();
        return officials.stream()
                .map(this::refactorResponse)  // Convert each Official to OfficialResponse
                .collect(Collectors.toList());
    }

    // Update official method with OfficialResponse
    public OfficialResponse updateOfficial(Long id, Official officialDetails) {
        Official official = officialRepository.findById(id)
                .orElseThrow(() -> new OfficialNotFoundException("Official not found with ID: " + id));

        User user = official.getUser();

        if (officialDetails.getUser() != null) {
            // Update the username if provided in the request body
            if (officialDetails.getUser().getUsername() != null && !officialDetails.getUser().getUsername().isEmpty()) {
                user.setUsername(officialDetails.getUser().getUsername());
            }

            // Update the email if provided in the request body
            if (officialDetails.getUser().getEmail() != null && !officialDetails.getUser().getEmail().isEmpty()) {
                user.setEmail(officialDetails.getUser().getEmail());
            }

            // Update the password if provided in the request body and encode it
            if (officialDetails.getUser().getPassword() != null && !officialDetails.getUser().getPassword().isEmpty()) {
                String encodedPassword = passwordEncoder.encode(officialDetails.getUser().getPassword());
                user.setPassword(encodedPassword);
            }

            // Save the updated user
            userRepository.save(user);
        }

        // Update official details
        official.setName(officialDetails.getName());
        official.setContactNo(officialDetails.getContactNo());
        official.setPosition(officialDetails.getPosition());
        Official updatedOfficial = officialRepository.save(official);

        return refactorResponse(updatedOfficial);
    }

    // Delete official method
    public void deleteOfficial(Long id) {
        Official official = officialRepository.findById(id)
                .orElseThrow(() -> new OfficialNotFoundException("Official not found with ID: " + id));
        officialRepository.delete(official);
    }

    // Method to convert Official to OfficialResponse
    private OfficialResponse refactorResponse(Official official) {
        OfficialResponse officialResponse = new OfficialResponse();
        officialResponse.setOfficialId(official.getId());
        officialResponse.setName(official.getName());
        officialResponse.setContactNo(official.getContactNo());
        officialResponse.setPosition(official.getPosition());
        officialResponse.setPassword(official.getUser().getPassword());
        officialResponse.setEmail(official.getUser().getEmail());
        officialResponse.setUsername(official.getUser().getUsername());

        // Assuming the official is associated with teams or events, get them

        return officialResponse;
    }
}


