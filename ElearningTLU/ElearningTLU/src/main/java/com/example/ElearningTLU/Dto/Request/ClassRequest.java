package com.example.ElearningTLU.Dto.Request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassRequest {
    private String CourseSemesterGroupId;
    List<LichHocRequest> lichHocRequestList = new ArrayList<>();
}
