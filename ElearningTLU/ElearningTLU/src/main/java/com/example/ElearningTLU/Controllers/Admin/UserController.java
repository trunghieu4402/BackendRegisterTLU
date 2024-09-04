package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Dto.PersonDto;
import com.example.ElearningTLU.Dto.StudentDto;
import com.example.ElearningTLU.Dto.TeacherDto;
import com.example.ElearningTLU.Entity.Student;
import com.example.ElearningTLU.Services.UserService.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/User")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
//    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String username = authentication.getName();

    @PostMapping("/createStudent")
    public ResponseEntity<?> createStudent(@RequestBody StudentDto personDto)
    {
        ResponseEntity<?> response =userService.CreateStudent(personDto);
        return response;
    }

    @GetMapping("")
    public ResponseEntity<?> getALlStudent()
    {
//        this.getUser();
//        System.out.println(authentication);
        return userService.GetAllStudent();
    }
    @GetMapping("/FindStudentById")
    public ResponseEntity<?> findStudentById(@Param("id") String id)
    {
        return this.userService.GetStudentById(id);
    }
    @DeleteMapping("/deleteStudent")
    public ResponseEntity<?> deleteStudent(@Param("id") String id)
    {
        return this.userService.deleteStudent(id);
    }
    @PutMapping("/updateStudent")
    public ResponseEntity<?> updateStudent(@RequestBody StudentDto studentDto)
    {
        return this.userService.updateStudent(studentDto);
    }
    @PostMapping("/createTeacher")
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDto Dto)
    {
        return userService.createTeacher(Dto);
    }
    @GetMapping("/getTeacher")
    public ResponseEntity<?> getTeacher(@RequestParam("id") String id)
    {
        return userService.getTeacherById(id);
    }


}
