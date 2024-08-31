package com.example.ElearningTLU.Dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class AutoAddClassRoom {
    private String courseSemesterGroupId;
    private List<Integer> start= new ArrayList<>();
    private List<Integer> finish= new ArrayList<>();
    private int countClass;

}
