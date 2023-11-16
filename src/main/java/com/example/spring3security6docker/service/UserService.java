package com.example.spring3security6docker.service;

import com.example.spring3security6docker.dao.entity.Role;
import com.example.spring3security6docker.dao.entity.User;
import com.example.spring3security6docker.dto.UserDto;
import com.example.spring3security6docker.dto.request.SignupRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public interface UserService {

    public User registerUser(SignupRequest signUpRequest);

    public Mono<Void> createUser(SignupRequest signUpRequest);

    public Mono<Set<Role>> getRolesByUsername(String username);

    public Set<Role> getRolesByUsernameToSet(String username) throws ExecutionException, InterruptedException;
}
