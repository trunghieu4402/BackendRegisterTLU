package com.example.ElearningTLU.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BaseCourse {
    @Id
    private String courseId;
    private String courseName;
    private int credits;
    private double coefficient;
}
