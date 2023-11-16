package com.example.spring3security6docker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableR2dbcRepositories
public class Spring3Security6DockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spring3Security6DockerApplication.class, args);
    }

}
