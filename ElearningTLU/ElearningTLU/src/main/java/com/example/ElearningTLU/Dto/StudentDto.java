package com.example.ElearningTLU.Dto;

import lombok.Data;

@Data
public class StudentDto extends PersonDto{
    private String departmentId;
    private String majorId;
    private boolean status;
}
