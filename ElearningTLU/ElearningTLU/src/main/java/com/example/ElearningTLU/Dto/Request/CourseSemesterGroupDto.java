package com.example.ElearningTLU.Dto.Request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseSemesterGroupDto {
    private String courseId;
    private String SemesterGroupId;
    private List<ClassRequest> classRequests = new ArrayList<>();
}
