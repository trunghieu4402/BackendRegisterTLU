package com.example.ElearningTLU.Services.RegisterService;

import org.springframework.http.ResponseEntity;

public interface RegisterService {
    public ResponseEntity<?> register(String person,String classroomId);
    public ResponseEntity<?> getAllCLass(String perId);

    public ResponseEntity<?> getPreSchedule(String userId);

    public ResponseEntity<?> removeClassRoom(String userId,String classRoomId);

}
