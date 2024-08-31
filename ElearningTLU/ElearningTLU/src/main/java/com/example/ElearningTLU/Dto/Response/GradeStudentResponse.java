package com.example.ElearningTLU.Dto.Response;

import lombok.Data;

import java.time.LocalDate;
@Data
public class GradeStudentResponse {

    private String studentId;
    private String studentName;
    private LocalDate birthDay;
    private float midScore;
    private float endScore;
}
