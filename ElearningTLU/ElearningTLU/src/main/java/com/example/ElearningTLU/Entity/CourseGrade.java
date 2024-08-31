package com.example.ElearningTLU.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class CourseGrade {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long Id;


   private String courseID;


   private String courseName;
   protected int credits;

   private float midScore;
   private float endScore;
   private float finalScore;
   private StatusCourse status;

   @ManyToOne
   @JoinColumn(name = "personId")
   private Student student;
}
