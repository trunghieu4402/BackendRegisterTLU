package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Services.SemesterService.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/semester")
public class SemesterController {
    @Autowired
    private SemesterService semesterService;
    @GetMapping("/getAllSemester")
    public ResponseEntity<?> getAllSemester()
    {
        return this.semesterService.getAllSemester();
    }
}
