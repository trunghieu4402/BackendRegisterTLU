package com.example.ElearningTLU.Services.ClassRoomService;

import org.springframework.http.ResponseEntity;

public interface RoomServiceImpl {
    public ResponseEntity<?> getAllRoom();
    public ResponseEntity<?> getAllRoomBySemester(String id);
}
