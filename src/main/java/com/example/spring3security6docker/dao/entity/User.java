package com.example.spring3security6docker.dao.entity;

import com.example.spring3security6docker.dto.request.SignupRequest;
//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

@Table("users")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User extends BaseEntity{

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Transient
    @ManyToMany
    @JoinTable(name = "user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
        schema = "courses"
    )
    private Set<Role> roles;

    public User(SignupRequest signUpRequest, String password) {
        this.username = signUpRequest.getEmail();
        this.lastName = signUpRequest.getLastName();
        this.firstName = signUpRequest.getFirstName();
        this.email = signUpRequest.getEmail();
        this.password = password;
        this.setCreated(LocalDateTime.now());
        this.setUpdated(LocalDateTime.now());
        this.setStatus(Status.NOT_ACTIVE);
    }
}
