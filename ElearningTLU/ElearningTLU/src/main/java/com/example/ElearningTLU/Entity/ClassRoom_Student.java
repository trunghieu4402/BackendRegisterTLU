package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "ClassRoom_Student")
public class ClassRoom_Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "classRoomId")
    @JsonIgnore
    private ClassRoom classRoom;

    @ManyToOne
    @JoinColumn(name = "studentId")
    private Student student;

    private float midScore;
    private float endScore;


}
