package com.example.ElearningTLU.Controllers.Student;

import com.example.ElearningTLU.Utils.JWTToken;
import com.example.ElearningTLU.Services.RegisterService.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/register")
public class RegisterController {
    @Autowired
    private RegisterService registerService;
    @Autowired
    private JWTToken token;

    @PostMapping("/add")
    public ResponseEntity<?> register(@RequestParam("id") String classroomId)
    {
        String person = this.token.getUser();
        return this.registerService.register(person,classroomId);
    }
    @DeleteMapping("/remove")
    public  ResponseEntity<?> remove(@RequestParam("id") String classRoomId)
    {
        String person= this.token.getUser();
        return this.registerService.removeClassRoom(person,classRoomId);

    }
    @GetMapping("")
    public ResponseEntity<?> getAll()
    {
        String perId= this.token.getUser();
        return this.registerService.getAllCLass(perId);
    }
    @GetMapping("/getRegister")
    public ResponseEntity<?> getPreSchedule()
    {
        String userId  = this.token.getUser();
        return this.registerService.getPreSchedule(userId);
    }

}
