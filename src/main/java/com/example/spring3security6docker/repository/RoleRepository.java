package com.example.spring3security6docker.repository;

import com.example.spring3security6docker.dao.entity.Role;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface RoleRepository extends R2dbcRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
