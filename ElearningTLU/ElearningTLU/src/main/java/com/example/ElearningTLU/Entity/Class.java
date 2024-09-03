package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ClassRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;
//mã môn + .+ version
    private String classRoomId;
//    private double version;
    private String name;

    @ManyToOne
    @JoinColumn(name = "courseSemesterGroupId")
    @JsonIgnore
    private Course_SemesterGroup courseSemesterGroup;

    @ManyToOne
    @JoinColumn(name = "roomId",nullable = false)
    @JsonIgnoreProperties("classRoomList")
    private Room room;
    //  for each class will have 60 slot  per week
    private Integer start ;
    private Integer finish ;

    private int currentSlot;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    @JsonIgnore
    @JsonIgnoreProperties({"leaddepartment","listClassRooms",""})
    private Teacher teacher;


    @OneToMany(mappedBy = "classRoom")
    List<ClassRoom_Student> classRoomStudents = new ArrayList<>();
}
