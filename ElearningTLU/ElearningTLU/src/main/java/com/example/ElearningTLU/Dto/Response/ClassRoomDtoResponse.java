package com.example.ElearningTLU.Dto.Response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassRoomDtoResponse {
    private String ClassRoomId;
    private int currentSlot;
    private int maxSlot;
    List<LichHocResponse> lichHocList = new ArrayList<>();
}
