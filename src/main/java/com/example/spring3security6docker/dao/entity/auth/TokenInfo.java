package com.example.spring3security6docker.dao.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    private Long userId;
    private String userName;
    private String token;
    private Date issuedAt;
    private Date expiresAt;
}
