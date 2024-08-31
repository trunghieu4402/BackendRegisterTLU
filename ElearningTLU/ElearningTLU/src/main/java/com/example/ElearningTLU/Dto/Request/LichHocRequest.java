package com.example.ElearningTLU.Dto.Request;

import lombok.Data;

@Data
public class LichHocRequest {
    private String Teacher;
    private String roomId;
    private Integer start;
    private Integer finish;
}
