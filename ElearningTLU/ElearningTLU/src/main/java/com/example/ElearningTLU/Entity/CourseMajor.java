package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CourseMajor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    @ManyToOne
    @JoinColumn(name = "courseId",nullable = false)
    @JsonIgnoreProperties({"listDepartment","listMajor","type"})
    private Course course;


    @ManyToOne
    @JoinColumn(name = "majorId",nullable = false)
    @JsonIgnore
    private Major major;
}
