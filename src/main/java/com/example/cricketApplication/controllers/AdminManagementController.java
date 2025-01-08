package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.Admin;
import com.example.cricketApplication.models.ERole;
import com.example.cricketApplication.models.Role;
import com.example.cricketApplication.models.User;
import com.example.cricketApplication.payload.request.SignupRequest;
import com.example.cricketApplication.payload.response.AdminResponse;
import com.example.cricketApplication.payload.response.CoachResponse;
import com.example.cricketApplication.payload.response.MessageResponse;
import com.example.cricketApplication.repository.AdminRepository;
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
public class AdminManagementController {

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    // 1. Get all admins
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        List<AdminResponse> admins = adminRepository.findAll().stream()
                .map(admin -> new AdminResponse(
                        admin.getAdminId(),
                        admin.getName(),
                        admin.getContactNo(),
                        admin.getUser().getEmail(),
                        null, // Do not send password in the response
                        admin.getUser().getUsername(),
                        admin.getCreatedBy(),
                        admin.getCreatedOn(),
                        admin.getUpdatedBy(),
                        admin.getUpdatedOn()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(admins);
    }

    // 2. Update an admin
    @PutMapping("/{adminId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateAdmin(@PathVariable Long adminId, @RequestBody SignupRequest updateRequest) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        User user = admin.getUser();

        // Validate uniqueness if username or email changes
        if (!user.getUsername().equals(updateRequest.getUsername()) &&
                userRepository.existsByUsername(updateRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (!user.getEmail().equals(updateRequest.getEmail()) &&
                userRepository.existsByEmail(updateRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Update the User part
        user.setUsername(updateRequest.getUsername());
        user.setEmail(updateRequest.getEmail());
        if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(updateRequest.getPassword()));
        }

        // Update the Admin part
        admin.setName(updateRequest.getName());
        admin.setContactNo(updateRequest.getContactNo());
        admin.setCreatedBy(updateRequest.getCreatedBy());
        admin.setCreatedOn(updateRequest.getCreatedOn());
        admin.setUpdatedBy(updateRequest.getUpdatedBy());
        admin.setUpdatedOn(updateRequest.getUpdatedOn());

        userRepository.save(user); // Save the user changes
        adminRepository.save(admin); // Save the admin changes

        return ResponseEntity.ok(new MessageResponse("Admin updated successfully!"));
    }

    // 3. Delete an admin
    @DeleteMapping("/{adminId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        adminRepository.delete(admin);
        User user = admin.getUser();
        userRepository.delete(user);
        return ResponseEntity.ok(new MessageResponse("Admin deleted successfully!"));
    }
}