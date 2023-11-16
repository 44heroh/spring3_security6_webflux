package com.example.spring3security6docker.rest.controller;

import com.example.spring3security6docker.dao.entity.User;
import com.example.spring3security6docker.dto.UserDto;
import com.example.spring3security6docker.dto.request.SignupRequest;
import com.example.spring3security6docker.mapper.UserMapper;
import com.example.spring3security6docker.repository.UserRepositoryR2;
import com.example.spring3security6docker.service.UserService;
import com.example.spring3security6docker.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepositoryR2 userRepository;
    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @PostMapping("/create")
    public ResponseEntity<?> addUser(@Valid @RequestBody SignupRequest signUpRequest) {
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
//        }

//        UserDto ans = userMapper.map(userService.registerUser(signUpRequest));

        return ResponseEntity.ok("");
    }

    @PostMapping("/add")
    public Mono<Void> add(@Valid @RequestBody SignupRequest signUpRequest) {
        return userService.createUser(signUpRequest);
    }
}
