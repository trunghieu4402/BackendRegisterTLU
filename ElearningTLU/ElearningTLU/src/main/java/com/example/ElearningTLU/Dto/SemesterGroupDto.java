package com.example.ElearningTLU.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SemesterGroupDto {
    private String SemesterGroupId;
    private String SemesterID;
    private String GroupID;
    private float  BaseCost;
    private String start;
    private String end;
    private String TimeDKHoc;
}
