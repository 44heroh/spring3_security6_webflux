package com.example.spring3security6docker.security;

import com.example.spring3security6docker.dao.entity.ERole;
import com.example.spring3security6docker.dao.entity.Role;
import com.example.spring3security6docker.service.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Component
public class UserAuthenticationBearer {

    private final UserServiceImpl userService;

    @Autowired
    public UserAuthenticationBearer(UserServiceImpl userService) {
        this.userService = userService;
    }

    public Mono<Authentication> create(JwtHandler.VerificationResult verificationResult) throws ExecutionException, InterruptedException {
        Claims claims = verificationResult.claims;
        String subject = claims.getSubject();

        String username = claims.get("username", String.class);

//        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<String> roleList = new ArrayList<>();

        Set<Role> roles = userService.getRolesByUsername(username).map(
                roleSet -> {
                    for (Role role : roleSet) {
                        roleList.add(role.getName());
                        authorities.add(new SimpleGrantedAuthority(role.getName()));
                    }
                    if(!roleList.contains(String.valueOf(ERole.ROLE_ADMIN))){
                        try {
                            throw new Exception("User is not Admin");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return roleSet;
                }
        ).switchIfEmpty(Mono.error(new Exception("Roles is null"))).toFuture().get();
//        try {
//            System.out.println("create roles => " + roles.toFuture().get().toString());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        Long principalId = Long.parseLong(subject);
        CustomPrincipal principal = new CustomPrincipal(principalId, username);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }
}
