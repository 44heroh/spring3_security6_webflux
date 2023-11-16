package com.example.spring3security6docker.repository;

import com.example.spring3security6docker.dao.entity.Course;
import com.example.spring3security6docker.dao.entity.Status;
import com.example.spring3security6docker.dao.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface CourseRepositoryR2 extends R2dbcRepository<Course, Long> {

    Mono<Course> findAllById(int id);

    Flux<Course> findAllByStatus(Status status);

    Flux<Course> findTop5ByStatus(Status status);
}
