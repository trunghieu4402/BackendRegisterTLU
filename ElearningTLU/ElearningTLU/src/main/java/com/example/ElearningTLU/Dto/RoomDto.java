package com.example.ElearningTLU.Dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomDto {
    private String roomId;
    private String roomName;
    private int seats;
    private List<Lop> lopList = new ArrayList<>();
}
