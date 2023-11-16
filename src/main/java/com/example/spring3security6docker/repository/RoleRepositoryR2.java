package com.example.spring3security6docker.repository;

import com.example.spring3security6docker.dao.entity.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepositoryR2 extends R2dbcRepository<Role, Long> {

    @Query("SELECT * FROM courses.roles WHERE name = :name;")
    Mono<Role> findByName(String name);

    @Query("SELECT courses.roles.id, courses.roles.created, courses.roles.updated, courses.roles.status, courses.roles.name  FROM courses.users LEFT JOIN courses.user_roles ON courses.users.id = courses.user_roles.user_id LEFT JOIN courses.roles ON courses.user_roles.role_id = courses.roles.id WHERE courses.users.username = :username;")
    Flux<Role> findRolesByUsername(String username);

    @Query("SELECT courses.roles.id, courses.roles.created, courses.roles.updated, courses.roles.status, courses.roles.name  FROM courses.users LEFT JOIN courses.user_roles ON courses.users.id = courses.user_roles.user_id LEFT JOIN courses.roles ON courses.user_roles.role_id = courses.roles.id WHERE courses.users.id = :userId;")
    Flux<Role> findRolesByUserId(int userId);
}
