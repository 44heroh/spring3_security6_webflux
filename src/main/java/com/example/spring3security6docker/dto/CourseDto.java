package com.example.spring3security6docker.dto;

import com.example.spring3security6docker.dao.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDto {

    private Long id;
    @NotNull(message = "Название не может быть пустым")
    private String author;
    @NotNull(message = "Название не может быть пустым")
    private String name;
    private Date created;
    private Date updated;
    private Status status;

    @Override
    public String toString() {
        return "CourseDto{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
