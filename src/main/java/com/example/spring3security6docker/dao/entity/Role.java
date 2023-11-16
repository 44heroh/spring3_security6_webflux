package com.example.spring3security6docker.dao.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Entity
@Table(name = "roles", schema = "courses")
@Data
public class Role extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                '}';
    }

//    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//    private List<User> users;

}
