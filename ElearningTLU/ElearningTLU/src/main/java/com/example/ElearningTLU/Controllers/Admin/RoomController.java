package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Services.ClassRoomService.RoomServiceImpl;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/Room")
public class RoomController {
    @Autowired
    private RoomServiceImpl roomService;

    @GetMapping()
    public ResponseEntity<?> getAllRoom()
    {
        return this.roomService.getAllRoom();
    }
    @GetMapping("/getAllRoom")
    public ResponseEntity<?> getAllRoomBySemester(@RequestParam("id") String id)
    {
        return this.roomService.getAllRoomBySemester(id);
    }

}
