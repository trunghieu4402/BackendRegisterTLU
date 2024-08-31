package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "majorId")
public class Major {
    @Id
    private String majorId;
    private String majorName;

    @ManyToOne
    @JoinColumn(name = "departmentId",nullable = false,referencedColumnName = "DepartmentId")
    @JsonIgnore
    private Department department;

    @OneToMany(mappedBy = "major")
    @JsonIgnore
    private List<Student> listStudents;


    @OneToMany(mappedBy = "major")
    @JsonIgnoreProperties({"major"})
    private List<CourseMajor> courses = new ArrayList<>();
}
