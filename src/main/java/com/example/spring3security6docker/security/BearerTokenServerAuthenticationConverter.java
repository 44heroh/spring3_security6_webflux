package com.example.spring3security6docker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtHandler jwtHandler;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final Function<String, Mono<String>> getBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(BEARER_PREFIX.length()));
    private final UserAuthenticationBearer userAuthenticationBearer;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractHeader(exchange)
                .flatMap(getBearerValue)
                .flatMap(token -> jwtHandler.check(token))
                .flatMap(verificationResult -> {
                    try {
                        return userAuthenticationBearer.create(verificationResult);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    private Mono<String> extractHeader(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest()
        .getHeaders()
        .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
