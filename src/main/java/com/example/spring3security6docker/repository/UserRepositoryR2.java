package com.example.spring3security6docker.repository;

import com.example.spring3security6docker.dao.entity.Role;
import com.example.spring3security6docker.dao.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserRepositoryR2 extends R2dbcRepository<User, Long> {

    @Query("SELECT * FROM courses.users WHERE username = :username LIMIT 1;")
    Mono<User> findByUsernameTop1(String username);

    @Query("SELECT * FROM courses.users WHERE id = :id;")
    Mono<User> findById(int id);

    @Query("SELECT 1 FROM courses.users WHERE username = ':username';")
    Mono<Boolean> existsByUsername(String username);

    Mono<Boolean> existsByEmail(String email);

    Mono<User> findByUsername(String username);
}
