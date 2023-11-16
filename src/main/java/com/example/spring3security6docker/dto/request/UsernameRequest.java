package com.example.spring3security6docker.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UsernameRequest {
    @NotBlank
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
