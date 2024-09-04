package com.example.ElearningTLU.Dto.Request;

import com.example.ElearningTLU.Entity.CourseType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseRequest {
    private String CourseId;
    private String CourseName;
    private int Credits;
    private double Coefficient;
    private CourseType type;
    private List<String> MajorId= new ArrayList<>();
    private List<String> DepartmentId = new ArrayList<>();
    private List<String> reqiId = new ArrayList<>();
    private int requestCredits;
}
