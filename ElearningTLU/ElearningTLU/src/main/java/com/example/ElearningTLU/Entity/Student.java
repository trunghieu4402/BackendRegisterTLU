package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Student extends Person {
    private float score;
    private Integer totalCredits;
    private boolean status;
    private LocalDate startStudyTime;
    private LocalDate finishStudyTime;


    @ManyToOne
    @JoinColumn(name = "majorId")
    @JsonIgnoreProperties({"courses"})
    private Major major;

    @ManyToOne
    @JoinColumn(name = "groupId")
    @JsonIgnoreProperties({"num"})
    private GroupStudent group;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    @JsonIgnore
    List<CourseGrade> courseGradeList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "generationId")
    private Generation generation;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    @JsonIgnore
    List<Invoice> invoiceList = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Class_Student> classRoomStudents = new ArrayList<>();

}
//