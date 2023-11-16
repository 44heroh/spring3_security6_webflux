package com.example.spring3security6docker.security;

import com.example.spring3security6docker.dao.entity.ERole;
import com.example.spring3security6docker.dao.entity.Role;
import com.example.spring3security6docker.dao.entity.Status;
import com.example.spring3security6docker.dao.entity.User;
import com.example.spring3security6docker.service.impl.UserServiceImpl;
import com.example.spring3security6docker.service.security.AuthException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;


    private TokenDetails generateToken(User user) {
//        Set<Role> roles = null;
//        try {
//            roles = userService.getRolesByUsername(user.getUsername()).toFuture().get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        Set<Role> finalRoles = roles;
        String[] strings = new String[]{String.valueOf(ERole.ROLE_ADMIN), String.valueOf(ERole.ROLE_USER)};
        Map<String, Object> claims = new HashMap<>() {{
//            put("role", Arrays.toString(strings));
            put("id", String.valueOf(user.getId()));
            put("username", user.getUsername());
        }};
        return generateToken(claims, user.getId().toString());
    }

    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        Long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(user -> {
                    if (user.getStatus() == Status.NOT_ACTIVE) {
                        return Mono.error(new AuthException("Account disabled", "PROSELYTE_USER_ACCOUNT_DISABLED"));
                    }

                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new AuthException("Invalid password", "PROSELYTE_INVALID_PASSWORD"));
                    }

                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("Invalid username", "PROSELYTE_INVALID_USERNAME")));
    }
}
