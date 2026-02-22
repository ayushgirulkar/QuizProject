package com.example.quizapp.dto;

import com.example.quizapp.model.Role;
import jakarta.validation.constraints.*;

public class AuthDTOs {
    public static class SignupRequest {
        @NotBlank public String name;
        @Email @NotBlank public String email;
        @NotBlank public String password;
        @NotNull public Role role;
    }
    public static class LoginRequest {
        @Email @NotBlank public String email;
        @NotBlank public String password;
    }
    public static class UserResponse {
        public Long id; public String name; public String email; public Role role;
        public UserResponse(Long id,String name,String email,Role role){
            this.id=id; this.name=name; this.email=email; this.role=role;
        }
    }
}
