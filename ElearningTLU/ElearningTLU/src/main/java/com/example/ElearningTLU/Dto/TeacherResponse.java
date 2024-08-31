package com.example.ElearningTLU.Dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class TeacherResponse {
    private String personId;
    private String fullName;
    private String departmentId;
    List<ScheduleDto> teachingScheduleList = new ArrayList<>();
}
