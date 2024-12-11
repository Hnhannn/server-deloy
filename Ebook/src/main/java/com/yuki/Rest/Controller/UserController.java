package com.yuki.Rest.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yuki.dto.LoginDto;
import com.yuki.dto.SignupClientDTO;
import com.yuki.dto.UserDto;
import com.yuki.entity.User;
import com.yuki.enums.Status;
import com.yuki.jwt.JwtResponse;
import com.yuki.jwt.JwtTokenProvider;
import com.yuki.jwt.TokenService;
import com.yuki.repositoty.UserDAO;
import com.yuki.service.UserResponse;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String LOGINSTATUS = "LoginStatus";

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserDAO userDAO, PasswordEncoder passwordEncoder, TokenService tokenService,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signup-client")
    public ResponseEntity<String> signupClient(@Valid @RequestBody SignupClientDTO signupClientDTO) {
        if (Boolean.TRUE.equals(userDAO.existsByUsername(signupClientDTO.getUsername()))) {
            return new ResponseEntity<>("Username is already taken for " + signupClientDTO.getUsername(),
                    HttpStatus.BAD_REQUEST);
        }
        boolean role = false;
        User user = new User();
        user.setUsername(signupClientDTO.getUsername());
        user.setEmail(signupClientDTO.getEmail());
        user.setRole(role);
        user.setStatus(Status.UNVERIFIED);
        user.setPassword(passwordEncoder.encode(signupClientDTO.getPassword()));
        userDAO.save(user);
        return new ResponseEntity<>("Users registered successfully", HttpStatus.OK);
    }

    @GetMapping("/edit/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userDAO.findByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping("/getuser")
    public ResponseEntity<UserResponse> getUser() {
        UserResponse response = new UserResponse();
        response.setUsers(userDAO.findAllActiveUsers());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getemailbyusername")
    public ResponseEntity<String> getEmailByUsername(@RequestParam String username) {
        try {
            Optional<String> email = userDAO.getEmailByUsername(username);
            if (email.isEmpty()) {
                logger.warn("Email not found for username: {}", username);
                return ResponseEntity.notFound().build();
            }

            logger.info("Email found for username: {}", username);
            return ResponseEntity.ok(email.get());
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Internal server error");
        }
    }

    @GetMapping("/check-login")
    public ResponseEntity<Map<String, Object>> checkLogin(
            @RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap(LOGINSTATUS, false));
        }

        String token = authorizationHeader.substring(7);

        try {
            String username = jwtTokenProvider.getUsernameFromToken(token);
            String role = jwtTokenProvider.getRoleFromToken(token);
            String status = jwtTokenProvider.getStatusFromToken(token);

            Map<String, Object> response = new HashMap<>();

            if (Status.ACTIVE.equals(status) || Status.UNVERIFIED.equals(status)) {
                response.put(LOGINSTATUS, true);
                response.put("username", username);
                response.put("scope", role);
            } else {
                response.put(LOGINSTATUS, false);
            }
            response.put("status", status);
            return ResponseEntity.ok(response);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap(LOGINSTATUS, false));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto, HttpSession session) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                            loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Optional<User> optionalUser = userDAO.findByUsername(loginDto.getUsername());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            User user = optionalUser.get();

            String role = user.isRole() ? "ADMIN" : "CLIENT";
            String status = user.getStatus();
            String jwtToken = jwtTokenProvider.generateToken(authentication, role, status);
            JwtResponse jwtResponse = new JwtResponse(jwtToken, role, status);
            return ResponseEntity.ok(jwtResponse);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUsers(@Valid @RequestBody List<UserDto> signUpDtos) {
        for (UserDto signUpDto : signUpDtos) {
            if (Boolean.TRUE.equals(userDAO.existsByUsername(signUpDto.getUsername()))) {
                return new ResponseEntity<>("Username is already taken for " + signUpDto.getUsername(),
                        HttpStatus.BAD_REQUEST);
            }

            if (Boolean.TRUE.equals(userDAO.existsByEmail(signUpDto.getEmail()))) {
                return new ResponseEntity<>("Email is already taken for " + signUpDto.getEmail(),
                        HttpStatus.BAD_REQUEST);
            }

            boolean role = signUpDto.isRole();

            User user = new User();
            user.setFullName(signUpDto.getFullName());
            user.setUsername(signUpDto.getUsername());
            user.setEmail(signUpDto.getEmail());
            user.setGender(signUpDto.isGender());
            user.setRole(role);
            user.setStatus(signUpDto.getStatus());
            user.setPhoneNumber(signUpDto.getPhoneNumber());
            user.setUserImage(signUpDto.getUserImage());
            user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

            userDAO.save(user);
        }

        return new ResponseEntity<>("Users registered successfully", HttpStatus.OK);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Integer id, @Valid @RequestBody UserDto signUpDto) {
        try {
            Optional<User> existingUserOpt = userDAO.findById(id);
            if (!existingUserOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            User existingUser = existingUserOpt.get();

            validateUniqueFields(existingUser, signUpDto);

            existingUser.setFullName(signUpDto.getFullName());
            existingUser.setEmail(signUpDto.getEmail());
            existingUser.setGender(signUpDto.isGender());
            existingUser.setPhoneNumber(signUpDto.getPhoneNumber());
            existingUser.setUserImage(signUpDto.getUserImage());
            existingUser.setRole(signUpDto.isRole());

            if (signUpDto.getStatus() != null) {
                existingUser.setStatus(signUpDto.getStatus());
            }

            if (signUpDto.getPassword() != null && !signUpDto.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            }

            userDAO.save(existingUser);
            return ResponseEntity.ok("User updated successfully");
        } catch (DataAccessException e) {
            logger.error("Error updating user with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user");
        }
    }

    private void validateUniqueFields(User existingUser, UserDto signUpDto) {
        if (!existingUser.getUsername().equals(signUpDto.getUsername())
                && Boolean.TRUE.equals(userDAO.existsByUsername(signUpDto.getUsername()))) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (!existingUser.getEmail().equals(signUpDto.getEmail())
                && Boolean.TRUE.equals(userDAO.existsByEmail(signUpDto.getEmail()))) {
            throw new IllegalArgumentException("Email is already taken");
        }
    }

    @PutMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        String usernameToken = jwtTokenProvider.getUsernameFromToken(token);
        try {
            Optional<User> optioncalUser = userDAO.findById(id);
            if (!optioncalUser.isPresent()) {
                return ResponseEntity.notFound().build();
            }


            User existingUser = optioncalUser.get();
            if (existingUser.getUsername().equals(usernameToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot delete your own account.");
            }
            existingUser.setStatus(Status.DELETED);

            userDAO.save(existingUser);
            return ResponseEntity.ok("User delete successfully");

        } catch (DataAccessException e) {
            logger.error("Error deleting user with ID " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            tokenService.invalidateToken(jwtToken);
            return ResponseEntity.ok("Logged out successfully");
        } catch (TokenInvalidException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            logger.error("Error while logging out: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while logging out");
        }
    }

    @GetMapping("/search/{fullname}")
    public ResponseEntity<List<User>> searchUsersByFullname(@PathVariable("fullname") String fullname) {
        List<User> users = userDAO.findUsersByFullnameContaining(fullname);
        return ResponseEntity.ok(users);
    }

    public class TokenInvalidException extends RuntimeException {
        public TokenInvalidException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            String erorrLog = "Validation error in field '" + fieldName + "': " + errorMessage;
            logger.error(erorrLog);
        });
        return errors;
    }

    public class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

}