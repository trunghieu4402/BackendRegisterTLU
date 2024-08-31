package com.example.ElearningTLU.Controllers.Student;

import com.example.ElearningTLU.Utils.JWTToken;
import com.example.ElearningTLU.Services.UserService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
public class InforController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private JWTToken token;
    public String getUsername()
    {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping("")
    public ResponseEntity<?> getInfor()
    {
        return this.userService.GetStudentById(getUsername());
    }

    @GetMapping("/getTrainingProgram")
    public ResponseEntity<?>getTRainingProgram()
    {
        String username = token.getUser();
        return this.userService.getTrainingProgram(username);
    }
    @GetMapping("/getTimeTable")
    public ResponseEntity<?>getScheduleBySemesterGroup(@RequestParam("semesterId") String id)
    {
        String userId= this.token.getUser();
        return this.userService.getTimeTableBySemester(userId,id);
    }
    @GetMapping("/getTranscript")
    public ResponseEntity<?> getTranscript()
    {
        String StudentId= this.token.getUser();
        return this.userService.getTranscript(StudentId);
    }
}
