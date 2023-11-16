package com.example.spring3security6docker.repository;

import com.example.spring3security6docker.dao.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

//    Optional<User> findByUsername(String name);

    Mono<User> findByUsername(String username);

//    Boolean existsByUsername(String username);

    boolean existsByUsername(String username);

//    Boolean existsByEmail(String email);

    boolean existsByEmail(String email);
}
