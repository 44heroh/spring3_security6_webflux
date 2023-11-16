package com.example.spring3security6docker.security;

import com.example.spring3security6docker.dao.entity.Status;
import com.example.spring3security6docker.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserServiceImpl userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId())
                .filter(user -> {return user.getStatus() == Status.ACTIVE;})
                .switchIfEmpty(Mono.error(new Exception("User disabled")))
                .map(user -> authentication);
    }
}
