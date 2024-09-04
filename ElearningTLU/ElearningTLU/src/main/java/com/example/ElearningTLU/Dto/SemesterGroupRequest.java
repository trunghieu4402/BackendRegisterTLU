package com.example.ElearningTLU.Dto;

import lombok.Data;

@Data
public class SemesterGroupRequest {
    private String SemesterGroupId;
//    private String SemesterID;
    private String GroupID;
    private float  BaseCost;
    private String start;
    private String end;
    private String TimeDKHoc;
}
