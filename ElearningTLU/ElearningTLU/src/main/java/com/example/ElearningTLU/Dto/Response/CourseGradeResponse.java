package com.example.ElearningTLU.Dto.Response;

import com.example.ElearningTLU.Entity.StatusCourse;
import lombok.Data;

@Data
public class CourseGradeResponse {
    private String courseID;
    private String courseName;
    private int credits;
    private float midScore;
    private float endScore;
    private float finalScore;
    private String status;
}
