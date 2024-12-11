package com.example.cricketApplication.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.example.cricketApplication.models.*;
import com.example.cricketApplication.payload.response.CoachResponse;
import com.example.cricketApplication.payload.response.NewsResponse;
import com.example.cricketApplication.repository.*;
import com.example.cricketApplication.security.WebConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.cricketApplication.payload.request.LoginRequest;
import com.example.cricketApplication.payload.request.SignupRequest;
import com.example.cricketApplication.payload.response.JwtResponse;
import com.example.cricketApplication.payload.response.MessageResponse;
import com.example.cricketApplication.security.jwt.JwtUtils;
import com.example.cricketApplication.security.services.UserDetailsImpl;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    CoachRepository coachRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    MembershipRepository membershipRepository;

    @Autowired
    OfficialRepository officialRepository;

//    private String IMAGE_DIRECTORY = WebConfig.getImageDirectory();
      private static final String IMAGE_DIRECTORY = "D:\\upload\\";


//    @PostMapping("/signin")
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//        // Authenticate the user
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
//
//        // Fetch the User entity from the database
//        User user = userRepository.findByUsername(userDetails.getUsername())
//                .orElseThrow(() -> new RuntimeException("Error: User not found."));
//
//        // Check if the user has the ROLE_PLAYER
//        if (roles.contains("ROLE_PLAYER")) {
//            Player player = playerRepository.findByUser(user)
//                    .orElseThrow(() -> new RuntimeException("Error: Player not found."));
//
//            // Check if the player has a membership and if it is active
//            Membership membership = player.getMembership();
//            if (membership == null || !membership.getIsActive()) {
//                return ResponseEntity
//                        .badRequest()
//                        .body(new MessageResponse("Error: Player's membership is expired or not available."));
//            }
//
//        }
//
//        // Generate JWT token if all checks passed
//        String jwt = jwtUtils.generateJwtToken(authentication);
//
//        return ResponseEntity.ok(new JwtResponse(jwt,
//                userDetails.getId(),
//                userDetails.getUsername(),
//                userDetails.getEmail(),
//                roles));
//    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Fetch the User entity from the database
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Long playerId = null;
        Long coachId = null;
        Long officialId = null;

        // Check if the user has the ROLE_PLAYER
        if (roles.contains("ROLE_PLAYER")) {
            Player player = playerRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Error: Player not found."));

            // Check if the player has a membership and if it is active
            Membership membership = player.getMembership();
            if (membership == null || !membership.getIsActive()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Player's membership is expired or not available."));
            }
            playerId = player.getPlayerId();  // Store playerId
        }

        // Check if the user has the ROLE_COACH
        if (roles.contains("ROLE_COACH")) {
            Coach coach = (Coach) coachRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Error: Coach not found."));
            coachId = coach.getCoachId();  // Store coachId
        }

        // Check if the user has the ROLE_OFFICIAL
        if (roles.contains("ROLE_OFFICIAL")) {
            Official official = officialRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Error: Official not found."));
            officialId = official.getId();  // Store officialId
        }

        // Generate JWT token if all checks passed
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                playerId,  // Include playerId
                coachId,   // Include coachId
                officialId // Include officialId
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Clear the security context to invalidate the authentication session
        SecurityContextHolder.clearContext();

        // Set the JWT token in response headers to be empty or removed
        response.setHeader("Authorization", "");

        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }






//    @PostMapping("/signupPlayer")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        // Create new user's account
//        Player user = new Player(
//                signUpRequest.getName(),
//                signUpRequest.getContactNo(),
//                signUpRequest.getBattingStyle(),
//                signUpRequest.getBowlingStyle(),
//                signUpRequest.getStatus(),
//                signUpRequest.getImage(),
//                signUpRequest.getPlayerRole()
//
//                );
//        //System.out.println(user.getName());
//
//        Set<String> strRoles = signUpRequest.getRoles();
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                    .orElseGet(() -> {
//                        Role newRole = new Role(ERole.ROLE_USER);
//                        roleRepository.save(newRole);
//                        return newRole;
//                    });
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "ROLE_ADMIN":
//                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                .orElseGet(() -> {
//                                    Role newRole = new Role(ERole.ROLE_ADMIN);
//                                    roleRepository.save(newRole);
//                                    return newRole;
//                                });
//                        roles.add(adminRole);
//                        break;
//
//                    case "ROLE_COACH":
//                        Role coachRole = roleRepository.findByName(ERole.ROLE_COACH)
//                                .orElseGet(() -> {
//                                    Role newRole = new Role(ERole.ROLE_COACH);
//                                    roleRepository.save(newRole);
//                                    return newRole;
//                                });
//                        roles.add(coachRole);
//                        break;
//
//                    case "ROLE_PLAYER":
//                        Role playerRole = roleRepository.findByName(ERole.ROLE_PLAYER)
//                                .orElseGet(() -> {
//                                    Role newRole = new Role(ERole.ROLE_PLAYER);
//                                    roleRepository.save(newRole);
//                                    return newRole;
//                                });
//                        roles.add(playerRole);
//                        break;
//
//                    default:
//                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                .orElseGet(() -> {
//                                    Role newRole = new Role(ERole.ROLE_USER);
//                                    roleRepository.save(newRole);
//                                    return newRole;
//                                });
//                        roles.add(userRole);
//                }
//            });
//        }
//        User newUser = new User();
//        newUser.setRoles(roles);
//        newUser.setUsername(signUpRequest.getUsername());
//        newUser.setEmail(signUpRequest.getEmail());
//        newUser.setPassword(
//                encoder.encode(signUpRequest.getPassword()));
//        user.setUser(newUser);
//        user.setMembership(signUpRequest.getMembership());
//        membershipRepository.save(signUpRequest.getMembership());
////        user.setMembership(signUpRequest.getMembership());
//        userRepository.save(newUser);
//        playerRepository.save(user); // Save the User entity first
//
//        // If the user has the coach role, save them to the Coach table
////        if (roles.stream().anyMatch(role -> role.getName().equals(ERole.ROLE_COACH))) {
////            Coach coach = new Coach();
////            coach.setEmail(savedUser.getEmail());
////            coach.setName(savedUser.getUsername());
////            coach.setRole(roleRepository.findByName(ERole.ROLE_COACH)
////                    .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
////            coach.setUser(savedUser);
////            coachRepository.save(coach);
////        }
//
//        // If the user has the player role, save them to the Player table
////        if (roles.stream().anyMatch(role -> role.getName().equals(ERole.ROLE_PLAYER))) {
////            Player player = new Player();
////            player.setEmail(savedUser.getEmail());
////            player.setName(savedUser.getName());
////            player.setBattingStyle(savedUser.getBattingStyle());
////            player.setBowlingStyle(savedUser.getBowlingStyle());
////            player.setContactNo(savedUser.getContactNo());
////            player.setImage(savedUser.getImage());
////            player.setStatus(savedUser.getStatus());
////            player.setPlayerRole(savedUser.getPlayerRole());
////            player.setRole(roleRepository.findByName(ERole.ROLE_PLAYER)
////                    .orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
////            player.setUser(savedUser);
////            playerRepository.save(player);
////        }
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }




//    //@PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/signupPlayer")
//    public ResponseEntity<?> registerPlayer(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//        // Save membership entity first
//        Membership membership = signUpRequest.getMembership();
//        if (membership != null) {
//            membership = membershipRepository.save(membership); // Save membership
//        }
//
//        // Set default role for player
//        Role playerRole = roleRepository.findByName(ERole.ROLE_PLAYER)
//                .orElseGet(() -> {
//                    Role newRole = new Role(ERole.ROLE_PLAYER);
//                    roleRepository.save(newRole);
//                    return newRole;
//                });
//
//        // Create and set user entity
//        User newUser = new User();
//        newUser.setUsername(signUpRequest.getUsername());
//        newUser.setEmail(signUpRequest.getEmail());
//        newUser.setPassword(encoder.encode(signUpRequest.getPassword()));
//
//        Set<Role> roles = new HashSet<>();
//        roles.add(playerRole);
//        newUser.setRoles(roles);
//
//        // Create new player's account
//        Player player = new Player(
//                signUpRequest.getName(),
//                signUpRequest.getContactNo(),
//                signUpRequest.getBattingStyle(),
//                signUpRequest.getBowlingStyle(),
//                signUpRequest.getStatus(),
//                signUpRequest.getImage(),
//                signUpRequest.getPlayerRole(),
//                membership,
//                signUpRequest.getEmail(),
//                signUpRequest.getDateOfBirth(),
//                playerRole // Set the role for the player
//        );
//
//        // Link the player to the user entity
//        player.setUser(newUser);
//        player.setCreatedBy(signUpRequest.getCreatedBy());
//        player.setCreatedOn(signUpRequest.getCreatedOn());
//
//        // Save the user and player entities
//        userRepository.save(newUser);
//        playerRepository.save(player);
//
//        return ResponseEntity.ok(new MessageResponse("Player registered successfully!"));
//    }


    @PostMapping(value = "/signupPlayer", consumes = "multipart/form-data")
    public ResponseEntity<?> registerPlayer(
            @RequestParam("userData") String userData,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            // Parse the JSON payload
            ObjectMapper objectMapper = new ObjectMapper();
            SignupRequest signUpRequest = objectMapper.readValue(userData, SignupRequest.class);

            // Check for duplicate username and email
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }

            // Save membership entity if provided
            Membership membership = signUpRequest.getMembership();
            if (membership != null) {
                membership = membershipRepository.save(membership);
            }

            // Set default role for player
            Role playerRole = roleRepository.findByName(ERole.ROLE_PLAYER)
                    .orElseGet(() -> {
                        Role newRole = new Role(ERole.ROLE_PLAYER);
                        roleRepository.save(newRole);
                        return newRole;
                    });

            // Save the image locally
            String fileName = signUpRequest.getUsername() + ".jpg"; // Adjust the extension as needed
            String imagePath = IMAGE_DIRECTORY + fileName;
            Files.write(Paths.get(imagePath), imageFile.getBytes());



            // Create and set user entity
            User newUser = new User();
            newUser.setUsername(signUpRequest.getUsername());
            newUser.setEmail(signUpRequest.getEmail());
            newUser.setPassword(encoder.encode(signUpRequest.getPassword()));

            Set<Role> role = new HashSet<>();
            role.add(playerRole);
            newUser.setRoles(role);

            // Create new player's account
            Player player = new Player(
                    signUpRequest.getName(),
                    signUpRequest.getContactNo(),
                    signUpRequest.getBattingStyle(),
                    signUpRequest.getBowlingStyle(),
                    signUpRequest.getStatus(),
                    //imagePath, // Save image path as URL
                    fileName,
                    signUpRequest.getPlayerRole(),
                    membership,
                    signUpRequest.getEmail(),
                    signUpRequest.getDateOfBirth(),
                    playerRole // Set the role for the player
            );

            // Link the player to the user entity
            player.setUser(newUser);
            player.setCreatedBy(signUpRequest.getCreatedBy());
            player.setCreatedOn(signUpRequest.getCreatedOn());

            // Save the user and player entities
            userRepository.save(newUser);
            playerRepository.save(player);

            return ResponseEntity.ok(new MessageResponse("Player registered successfully!"));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }





    //@PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/signupCoach")
//    public ResponseEntity<?> registerCoach(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already in use!"));
//        }
//
//
//
//        // Create and set user entity
//        User newUser = new User();
//        newUser.setUsername(signUpRequest.getUsername());
//        newUser.setEmail(signUpRequest.getEmail());
//        newUser.setPassword(encoder.encode(signUpRequest.getPassword()));
//
//        // Set default role for coach
//        Role coachRole = roleRepository.findByName(ERole.ROLE_COACH)
//                .orElseGet(() -> {
//                    Role newRole = new Role(ERole.ROLE_COACH);
//                    roleRepository.save(newRole);
//                    return newRole;
//                });
//        Set<Role> roles = new HashSet<>();
//        roles.add(coachRole);
//        newUser.setRoles(roles);
//
//        // Create new user's account
//        Coach coach = new Coach();
//        coach.setName(signUpRequest.getName());
//        coach.setContactNo(signUpRequest.getContactNo());
//        coach.setEmail(signUpRequest.getEmail());
//        coach.setImage(signUpRequest.getImage());
//        coach.setDateOfBirth(signUpRequest.getDateOfBirth());
//        coach.setAddress(signUpRequest.getAddress());
//        coach.setDescription(signUpRequest.getDescription());
//        coach.setRole(coachRole);// Set other coach-specific fields as needed
//        coach.setCreatedBy(signUpRequest.getCreatedBy());
//        coach.setCreatedOn(signUpRequest.getCreatedOn());
//        coach.setStatus(signUpRequest.getStatus());
//
//        // Link the coach to the user entity
//        coach.setUser(newUser);
//
//        // Save the user and coach entities
//        userRepository.save(newUser);
//        coachRepository.save(coach);
//
//        return ResponseEntity.ok(new CoachResponse(coach));
//    }

    @PostMapping(value = "/signupCoach", consumes = "multipart/form-data")
    public ResponseEntity<?> registerCoach(
            @RequestParam("userData") String userData,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            // Parse the JSON payload
            ObjectMapper objectMapper = new ObjectMapper();
            SignupRequest signUpRequest = objectMapper.readValue(userData, SignupRequest.class);

            // Check for duplicate username and email
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
            }

            // Save the image locally
            String fileName = signUpRequest.getUsername() + ".jpg"; // Adjust the extension as needed
            String imagePath = IMAGE_DIRECTORY + fileName;
            Files.write(Paths.get(imagePath), imageFile.getBytes());

            // Set default role for coach
            Role coachRole = roleRepository.findByName(ERole.ROLE_COACH)
                    .orElseGet(() -> {
                        Role newRole = new Role(ERole.ROLE_COACH);
                        roleRepository.save(newRole);
                        return newRole;
                    });

            // Create and set user entity
            User newUser = new User();
            newUser.setUsername(signUpRequest.getUsername());
            newUser.setEmail(signUpRequest.getEmail());
            newUser.setPassword(encoder.encode(signUpRequest.getPassword()));

            Set<Role> roles = new HashSet<>();
            roles.add(coachRole);
            newUser.setRoles(roles);

            // Create new coach's account
            Coach coach = new Coach();
            coach.setName(signUpRequest.getName());
            coach.setContactNo(signUpRequest.getContactNo());
            coach.setEmail(signUpRequest.getEmail());
            coach.setImage(fileName); // Save image filename as reference
            coach.setDateOfBirth(signUpRequest.getDateOfBirth());
            coach.setAddress(signUpRequest.getAddress());
            coach.setDescription(signUpRequest.getDescription());
            coach.setRole(coachRole); // Set the role for the coach
            coach.setCreatedBy(signUpRequest.getCreatedBy());
            coach.setCreatedOn(signUpRequest.getCreatedOn());
            coach.setStatus(signUpRequest.getStatus());

            // Link the coach to the user entity
            coach.setUser(newUser);

            // Save the user and coach entities
            userRepository.save(newUser);
            coachRepository.save(coach);

            return ResponseEntity.ok(new MessageResponse("Coach registered successfully!"));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }



    @PostMapping("/signup")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
        // Check if the username already exists
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check if the email already exists
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create and set user entity
        User newUser = new User();
        newUser.setUsername(signUpRequest.getUsername());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPassword(encoder.encode(signUpRequest.getPassword()));

        // Set default role for admin
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseGet(() -> {
                    Role newRole = new Role(ERole.ROLE_ADMIN);
                    roleRepository.save(newRole);
                    return newRole;
                });

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        newUser.setRoles(roles);

        // Save the user entity with admin role
        userRepository.save(newUser);

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }



    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/signupOfficial")
    public ResponseEntity<?> registerOfficial(@Valid @RequestBody SignupRequest signUpRequest) {
        // Check if the username already exists
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Check if the email already exists
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create and set user entity
        User newUser = new User();
        newUser.setUsername(signUpRequest.getUsername());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPassword(encoder.encode(signUpRequest.getPassword()));

        // Set default role for official
        Role officialRole = roleRepository.findByName(ERole.ROLE_OFFICIAL)
                .orElseGet(() -> {
                    Role newRole = new Role(ERole.ROLE_OFFICIAL);
                    roleRepository.save(newRole);
                    return newRole;
                });

        Set<Role> roles = new HashSet<>();
        roles.add(officialRole);
        newUser.setRoles(roles);

        // Create new official account
        Official official = new Official();
        official.setName(signUpRequest.getName());
        official.setContactNo(signUpRequest.getContactNo());
        official.setPosition(signUpRequest.getPosition());
        official.setCreatedBy(signUpRequest.getCreatedBy());
        official.setCreatedOn(signUpRequest.getCreatedOn());

        // Link the official to the user entity
        official.setUser(newUser);

        // Save the user and official entities
        userRepository.save(newUser);
        officialRepository.save(official);

        return ResponseEntity.ok(new MessageResponse("Official registered successfully!"));
    }


    @GetMapping("/checkAvailability")
    public ResponseEntity<?> checkAvailability(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String email) {

        Map<String, Boolean> response = new HashMap<>();

        if (username != null && !username.isEmpty()) {
            response.put("usernameExists", userRepository.existsByUsername(username));
        }

        if (email != null && !email.isEmpty()) {
            response.put("emailExists", userRepository.existsByEmail(email));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<?> checkUsernameAvailability(
            @RequestParam(value = "username", required = false) String username) {

        Map<String, Boolean> response = new HashMap<>();

        if (username != null && !username.isEmpty()) {
            response.put("usernameExists", userRepository.existsByUsername(username));
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<?> checkEmailAvailability(
            @RequestParam(value = "email", required = false) String email) {

        Map<String, Boolean> response = new HashMap<>();

        if (email != null && !email.isEmpty()) {
            response.put("emailExists", userRepository.existsByEmail(email));
        }

        return ResponseEntity.ok(response);
    }

}






