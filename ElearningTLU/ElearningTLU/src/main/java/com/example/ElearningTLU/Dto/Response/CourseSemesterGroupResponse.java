package com.example.ElearningTLU.Dto.Response;

import com.example.ElearningTLU.Dto.Request.ClassRoomDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CourseSemesterGroupResponse {
    private String Id;
    private String courseId;
    private String courseName;
    private String semesterGroupId;
    private List<ClassRoomDtoResponse> classRoomDtos= new ArrayList<>();
}
