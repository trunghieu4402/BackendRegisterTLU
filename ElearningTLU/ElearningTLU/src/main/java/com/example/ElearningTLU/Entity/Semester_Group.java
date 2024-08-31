package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
//@Table(name = "Semester_Group")
public class Semester_Group {
    @Id
    private String semesterGroupId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "semesterId",nullable = false)
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "groupId",nullable = false)
    @JsonIgnore
    private GroupStudent group;

    private float baseCost;
    @Column(nullable = false)
    private LocalDate start;
    @Column(nullable = false)
    private LocalDate finish;

    private LocalDate timeDangKyHoc;
    private boolean active;

    @OneToMany(mappedBy = "semesterGroup")

    List<Course_SemesterGroup> courseSemesterList = new ArrayList<>();

}
