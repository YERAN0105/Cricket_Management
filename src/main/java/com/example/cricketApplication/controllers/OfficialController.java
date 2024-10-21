package com.example.cricketApplication.controllers;

import com.example.cricketApplication.models.ERole;
import com.example.cricketApplication.models.Official;
import com.example.cricketApplication.models.User;
import com.example.cricketApplication.payload.request.SignupRequest;
import com.example.cricketApplication.payload.response.MessageResponse;
import com.example.cricketApplication.payload.response.OfficialResponse;
import com.example.cricketApplication.repository.RoleRepository;
import com.example.cricketApplication.repository.UserRepository;
import com.example.cricketApplication.security.services.OfficialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/officials")
public class OfficialController {

    @Autowired
    private OfficialService officialService;



    // Get official by id with OfficialResponse
    @GetMapping("/{id}")
    public ResponseEntity<OfficialResponse> getOfficialById(@PathVariable Long id) {
        OfficialResponse officialResponse = officialService.getOfficialResponseById(id);
        return ResponseEntity.ok(officialResponse);
    }

    // Get all officials with OfficialResponse
    @GetMapping("/all")
    public ResponseEntity<List<OfficialResponse>> getAllOfficials() {
        List<OfficialResponse> officials = officialService.getAllOfficialResponses();
        return ResponseEntity.ok(officials);
    }

    // Update official by id with OfficialResponse
    @PutMapping("/update/{id}")
    public ResponseEntity<OfficialResponse> updateOfficial(@PathVariable Long id, @RequestBody Official officialDetails) {
        OfficialResponse updatedOfficial = officialService.updateOfficial(id, officialDetails);
        return ResponseEntity.ok(updatedOfficial);
    }

    // Delete official
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOfficial(@PathVariable Long id) {
        officialService.deleteOfficial(id);
        return ResponseEntity.noContent().build();
    }
}


