package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class StatisticsStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
    @OneToOne
    @JoinColumn(name = "courseId")
    @JsonIgnore
    private Course course;
    private int numberOfStudent;
}
