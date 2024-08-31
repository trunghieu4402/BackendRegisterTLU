package com.example.ElearningTLU.Dto.Request;

import lombok.Data;

@Data
public class GradeStudentRequest {
    private String studentId;
    private String classRoomId;
    private String semesterId;
    private float midScore;
    private float endScore;
}
