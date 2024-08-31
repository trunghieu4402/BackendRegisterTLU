package com.example.ElearningTLU.Dto.Response;

import lombok.Data;

@Data
public class TimeTableResponse {
    private String TeacherId;
    private String classRoomId;
    private String classRoomName;
    private String roomId;
    private int start;
    private int end;
}
