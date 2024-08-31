package com.example.ElearningTLU.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class TimeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String semesterGroupId;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person person;
    private String teacherId;
    private String classRoomId;
    private String classRoomName;
    private String roomId;
    private int start;
    private int end;
}
