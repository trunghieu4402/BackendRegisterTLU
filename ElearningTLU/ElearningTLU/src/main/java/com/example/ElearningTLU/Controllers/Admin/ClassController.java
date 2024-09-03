package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Dto.AutoAddClassRoom;
import com.example.ElearningTLU.Dto.Request.ClassRoomDto;
import com.example.ElearningTLU.Services.ClassRoomService.ClassRoomServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ClassRoom")
public class ClassRoomController {
@Autowired
    private ClassRoomServiceImpl classRoomService;

    @PostMapping("/addClassroom")
    private ResponseEntity<?> add(@RequestBody ClassRoomDto classRoomDto)
    {
       return this.classRoomService.createClassRoom(classRoomDto);
    }
    @PostMapping("/AutoAddClassRoom")
    private ResponseEntity<?> AutoAdd(@RequestBody AutoAddClassRoom classRoom)
    {
        return this.classRoomService.autoAddClassRoom(classRoom);
    }
    @GetMapping("/getClassRoom")
    private ResponseEntity<?> get(@RequestParam("id") String id)
    {
        return this.classRoomService.getClassRoom(id);
    }
    @GetMapping("/getAllRoomBySemester")
    private ResponseEntity<?> getAllRoom(@Param("id") String id)
    {
        return this.classRoomService.getAllClassRoomBySemester(id);
    }
    @GetMapping("/getAllClassRoom")
    private ResponseEntity<?> getAllClassRoomByCourseId(@RequestParam("id") String id)
    {
         return this.classRoomService.getAllClassroomByCourseId(id);
    }
}
