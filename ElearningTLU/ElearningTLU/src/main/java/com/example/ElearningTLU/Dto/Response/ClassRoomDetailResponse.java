package com.example.ElearningTLU.Dto.Response;

import com.example.ElearningTLU.Dto.StudentDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;

@Data
public class ClassRoomDetailResponse {
    private String classRoomId;
    private String classRoomName;
    private String semesterGroupId;
    private int start;
    private int finish;
    private List<GradeStudentResponse>studentList = new ArrayList<>();
}
