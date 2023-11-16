package com.example.spring3security6docker.dao.entity;

import com.example.spring3security6docker.dto.request.CourseCreateRequestDto;
import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Entity
@Table(name = "course", schema = "courses")
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
public class Course extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "author")
    private String author;

    @Column(name = "name")
    private String name;

    public Course(CourseCreateRequestDto courseCreateRequestDto) {
        this.author = courseCreateRequestDto.getAuthor();
        this.name = courseCreateRequestDto.getName();
        this.setCreated(LocalDateTime.now());
        this.setUpdated(LocalDateTime.now());
        this.setStatus(Status.NOT_ACTIVE);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
