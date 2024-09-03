package com.example.ElearningTLU.Dto.Request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassRoomDto {
    private String CourseSemesterGroupId;
    List<LichHocRequest> lichHocRequestList = new ArrayList<>();
}
