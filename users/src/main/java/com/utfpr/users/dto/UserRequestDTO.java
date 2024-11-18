package com.utfpr.users.dto;

import jakarta.validation.constraints.*;

public class UserRequestDTO {

    @NotBlank(message = "Username is mandatory.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @NotBlank(message = "Password is mandatory.")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    @Email(message = "Email must be valid.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}