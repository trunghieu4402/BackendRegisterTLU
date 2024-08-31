package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Dto.Request.CourseSemesterGroupDto;
import com.example.ElearningTLU.Services.CourseSemesterGroupService.CourseSemesterGroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/CourseSemester")
public class CourseSemesterGroupController {
    @Autowired
    private CourseSemesterGroupServiceImpl CouSemesterGroupService;

    @PostMapping("/addNewCourseSemesterGroup")
    private ResponseEntity<?> add(@RequestBody CourseSemesterGroupDto dto)
    {
        return this.CouSemesterGroupService.add(dto);
    }
    @GetMapping("/getAllCourseSemesterGroup")
    private ResponseEntity<?> getAll(@RequestParam("id") String id)
    {
        return this.CouSemesterGroupService.getAllBySemesterGroup(id);
    }
}
