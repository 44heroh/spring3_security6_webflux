package com.example.spring3security6docker.config;

import com.example.spring3security6docker.security.AuthenticationManager;
import com.example.spring3security6docker.security.BearerTokenServerAuthenticationConverter;
import com.example.spring3security6docker.security.JwtHandler;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
import com.example.spring3security6docker.security.UserAuthenticationBearer;
import com.example.spring3security6docker.service.impl.UserServiceImpl;
import com.example.spring3security6docker.service.security.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
//@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.example.spring3security6docker")
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private final UserAuthenticationBearer userAuthenticationBearer;

    private final String [] publicRoutes = {
            "/api/auth/register",
            "/api/auth/login",
            "/api/user/add",
            "/api/test/courses/pager/post",
            "/api/test/getroles"
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
        return http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS)
                .permitAll()
//                .pathMatchers(HttpMethod.POST)
//                .permitAll()
//                .pathMatchers(HttpMethod.GET)
//                .permitAll()
//                .pathMatchers(HttpMethod.PUT)
//                .permitAll()
//                .pathMatchers(HttpMethod.DELETE)
//                .permitAll()
//                .pathMatchers(HttpMethod.HEAD)
//                .permitAll()
                .pathMatchers(publicRoutes)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .exceptionHandling()
//                .authenticationEntryPoint((swe , e) -> {
//                    log.error("IN securityWebFilterChain - unauthorized error: {}", e.getMessage());
//                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
//                })
                .authenticationEntryPoint((swe , e) -> {
                    log.error("IN securityWebFilterChain - unauthorized error: {}", e.getMessage());
                    return Mono.error(new AuthException("Unauthorized", "UNAUTHORIZED"));
                })
//                .accessDeniedHandler((swe, e) -> {
//                    log.error("IN securityWebFilterChain - access denied: {}", e.getMessage());
//
//                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
//                })
                .accessDeniedHandler((swe, e) -> {
                    log.error("IN securityWebFilterChain - access denied: {}", e.getMessage());

                    return Mono.error(new AuthException("Access denied", "ACCESS_DENIED"));
                })
                .and()
                .addFilterAt(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter bearerAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(jwtHandlerInit(), userAuthenticationBearer));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthenticationFilter;
    }

    @Bean
    public JwtHandler jwtHandlerInit() {
        return new JwtHandler(secret);
    }
}
