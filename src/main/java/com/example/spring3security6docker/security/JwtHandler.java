package com.example.spring3security6docker.security;

import com.example.spring3security6docker.dao.entity.ERole;
import com.example.spring3security6docker.dao.entity.Role;
import com.example.spring3security6docker.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

public class JwtHandler {

    private String secret;

    public JwtHandler(
            String secret
    ) {
        this.secret = secret;
    }

    public Mono<VerificationResult> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new Exception(e.getMessage())));
    }

    private VerificationResult verify(String token) {
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();

//        Mono<Set<Role>> roles = userService.getRolesByUsername(claims.get("username").toString());
//        roles.map( roleSet -> {
//            List<String> roleList = new ArrayList<>();
//            for (Role role : roleSet) {
//                roleList.add(role.getName());
//            }
//            if(!roleList.contains(String.valueOf(ERole.ROLE_ADMIN))){
//                try {
//                    throw new Exception("User is not Admin");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return roleSet;
//        });

        if (expirationDate.before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(claims, token);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public static class VerificationResult {
        public Claims claims;
        public String token;

        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }
}
