package com.example.ElearningTLU.Dto;

import com.example.ElearningTLU.Dto.Request.LichHocRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ClassRoomResponse {

    private String ClassRoomId;
    private String ClassRoomName;
    private List<LichHocRequest> lichHocRequestList = new ArrayList<>();
}
