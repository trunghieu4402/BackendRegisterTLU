package com.example.ElearningTLU.Dto.Response;

import com.example.ElearningTLU.Entity.CourseType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CourseDtoResponse {
    private String courseId;
    private String courseName;
    private int credits;
    private double coefficient;
    private String type;
    private List<String> reqiId = new ArrayList<>();
    private int requestCredits;
}
