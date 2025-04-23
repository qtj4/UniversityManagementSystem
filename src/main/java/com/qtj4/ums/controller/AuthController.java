package com.qtj4.ums.controller;

import com.qtj4.ums.dto.AuthRequest;
import com.qtj4.ums.dto.AuthResponse;
import com.qtj4.ums.dto.RegisterRequest;
import com.qtj4.ums.model.User;
import com.qtj4.ums.repository.UserRepository;
import com.qtj4.ums.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("Registering user: {}", request.getUsername());
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            logger.warn("Username already taken: {}", request.getUsername());
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
        logger.info("User registered successfully: {}", request.getUsername());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        logger.info("Authenticating user: {}", request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String token = jwtUtil.generateToken(authentication.getName());
        logger.info("Login successful for user: {}", request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}