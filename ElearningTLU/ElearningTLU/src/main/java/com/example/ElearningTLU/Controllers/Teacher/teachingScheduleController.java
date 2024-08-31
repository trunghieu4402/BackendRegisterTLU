package com.example.ElearningTLU.Controllers.Teacher;

import com.example.ElearningTLU.Dto.Request.GradeStudentRequest;
import com.example.ElearningTLU.Services.UserService.UserServiceImpl;
import com.example.ElearningTLU.Utils.JWTToken;
import com.example.ElearningTLU.Services.TeachingScheduleService.TeachingScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
public class teachingScheduleController {
@Autowired
    private TeachingScheduleServiceImpl teachingScheduleService;

@Autowired
private UserServiceImpl userService;
@Autowired
private JWTToken token;


@GetMapping("/getSchedule")

    private ResponseEntity<?> getTimeTable(@RequestParam("semester") String semester)
{
    String user = this.token.getUser();
    return this.userService.getTimeTableBySemester(user,semester);
}

@GetMapping("/getDetailClass")
    public ResponseEntity<?> getStudentListByClassRoom(@RequestParam("ClassId") String id,@RequestParam("semesterGroupId") String semesterGroupId)
{
    String teacherId = this.token.getUser();
    return this.teachingScheduleService.getStudentListByClassRoom(teacherId,id,semesterGroupId);
}

@PutMapping("/updateStudentScore")
    public ResponseEntity<?> updateStudentScore(@RequestBody GradeStudentRequest gradeStudentRequest)
{
    String teacherId= this.token.getUser();
    return this.teachingScheduleService.updateStudentScore(teacherId,gradeStudentRequest);
}
}
