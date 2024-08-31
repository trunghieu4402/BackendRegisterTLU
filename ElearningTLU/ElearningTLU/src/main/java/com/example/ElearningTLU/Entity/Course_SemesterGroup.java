package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Course_SemesterGroup {
    @Id
    //id=mamon+maKyHoc
    private String courseSemesterGroupId;

//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "Course")
//    private Course course;

    private String courseId;
    private String courseName;
    private int credits;
    private double coefficient;
    private CourseType type;


    @ManyToOne
    @JoinColumn(name = "semesterGroupId")
    @JsonIgnore
    private Semester_Group semesterGroup;

    @OneToMany(mappedBy = "courseSemesterGroup")

    private List<ClassRoom> classRoomList = new ArrayList<>();

}
