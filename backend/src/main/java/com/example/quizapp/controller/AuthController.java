package com.example.quizapp.controller;

import com.example.quizapp.dto.AuthDTOs.*;
import com.example.quizapp.exception.ApiException;
import com.example.quizapp.model.User;
import com.example.quizapp.repo.UserRepository;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;

    public AuthController(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/signup")
    public UserResponse signup(@Valid @RequestBody SignupRequest req) {
        if (userRepo.findByEmail(req.email).isPresent()) {
            throw new ApiException("Email already exists");
        }

        User u = new User();
        u.setName(req.name);
        u.setEmail(req.email);
        u.setPassword(req.password);
        u.setRole(req.role);

        userRepo.save(u);

        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getRole()
        );
    }

    @PostMapping("/login")
    public UserResponse login(@Valid @RequestBody LoginRequest req) {
        User u = userRepo.findByEmail(req.email)
                .orElseThrow(() -> new ApiException("Invalid credentials"));

        if (!u.getPassword().equals(req.password)) {
            throw new ApiException("Invalid credentials");
        }

        return new UserResponse(
                u.getId(),
                u.getName(),
                u.getEmail(),
                u.getRole()
        );
    }
}
