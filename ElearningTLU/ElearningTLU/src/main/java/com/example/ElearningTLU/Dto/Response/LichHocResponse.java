package com.example.ElearningTLU.Dto.Response;

import com.example.ElearningTLU.Dto.Response.TeacherResponse;
import lombok.Data;

@Data
public class LichHocResponse {
    private TeacherResponse Teacher;
    private String roomId;
    private Integer start;
    private Integer finish;
}
