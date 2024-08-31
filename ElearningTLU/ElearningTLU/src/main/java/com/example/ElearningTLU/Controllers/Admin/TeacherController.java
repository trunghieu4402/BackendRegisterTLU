package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Services.TeachingScheduleService.TeachingScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class TeacherController {
    @Autowired
    private TeachingScheduleServiceImpl teachingScheduleService;

    @GetMapping("/getTeacherBySemester")
    private ResponseEntity<?> getTeacher(@RequestParam("semester") String semester)
    {
       return this.teachingScheduleService.getTeacherBySemester(semester);
    }
}
