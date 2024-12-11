package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.ERole;
import com.example.cricketApplication.models.Role;
import com.example.cricketApplication.models.User;
import com.example.cricketApplication.payload.request.SignupRequest;
import com.example.cricketApplication.payload.response.MessageResponse;
import com.example.cricketApplication.repository.RoleRepository;
import com.example.cricketApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
//@PreAuthorize("hasRole('ADMIN') and authentication.principal.username == 'admin01'") // Only allow admins
public class AdminManagementController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    // 1. Get all admins
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllAdmins() {
        // Assuming your User entity has a roles field and you filter by ROLE_ADMIN
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        List<User> admins = userRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(adminRole))
                .collect(Collectors.toList());

        return ResponseEntity.ok(admins);
    }

    // 2. Update an admin
    @PutMapping("/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long adminId, @RequestBody SignupRequest updateRequest) {
        User existingUser = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate uniqueness if username or email changes
        if (!existingUser.getUsername().equals(updateRequest.getUsername()) &&
                userRepository.existsByUsername(updateRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (!existingUser.getEmail().equals(updateRequest.getEmail()) &&
                userRepository.existsByEmail(updateRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        existingUser.setUsername(updateRequest.getUsername());
        existingUser.setEmail(updateRequest.getEmail());

        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(updateRequest.getPassword()));
        }

        userRepository.save(existingUser);

        return ResponseEntity.ok(new MessageResponse("Admin updated successfully!"));
    }

    // 3. Delete an admin
    @DeleteMapping("/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long adminId) {
        User existingUser = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(existingUser);
        return ResponseEntity.ok(new MessageResponse("Admin deleted successfully!"));
    }
}

