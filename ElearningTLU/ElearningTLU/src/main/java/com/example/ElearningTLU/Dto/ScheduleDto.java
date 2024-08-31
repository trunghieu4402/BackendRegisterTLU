package com.example.ElearningTLU.Dto;

import lombok.Data;

@Data
public class ScheduleDto {
    private String SemesterGroupId;
    private String classroomID;
    private String roomID;
    private int start;
    private int end;
}
