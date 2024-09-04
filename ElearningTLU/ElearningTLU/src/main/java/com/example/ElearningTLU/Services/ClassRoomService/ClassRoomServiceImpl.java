package com.example.ElearningTLU.Services.ClassRoomService;

import com.example.ElearningTLU.Dto.AutoAddClassRoom;
import com.example.ElearningTLU.Dto.Request.ClassRequest;
import org.springframework.http.ResponseEntity;

public interface ClassRoomServiceImpl {
    public ResponseEntity<?> createClassRoom(ClassRequest classRequest);
    public ResponseEntity<?> getClassRoom(String id);
    public ResponseEntity<?> getAllClassRoomBySemester(String id);
    public ResponseEntity<?> autoAddClassRoom(AutoAddClassRoom classRoom);

    public ResponseEntity<?> getAllClassroomByCourseId(String id);
}
