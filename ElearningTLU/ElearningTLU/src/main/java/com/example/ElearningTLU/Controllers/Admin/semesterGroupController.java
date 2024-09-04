package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Dto.SemesterGroupRequest;
import com.example.ElearningTLU.Services.SemesterService.SemesterGroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/SemesterGroup")
public class semesterGroupController {
    @Autowired
    private SemesterGroupServiceImpl semesterGroupService;

    @PostMapping("/addSemesterGroup")
    public ResponseEntity<?> addSemesterGroup(@RequestBody SemesterGroupRequest semesterGroupRequest)
    {
        return this.semesterGroupService.addSemesterGroup(semesterGroupRequest);
    }
    @GetMapping("/getAllSemesterGroup")
    public ResponseEntity<?> getAll()
    {
        return this.semesterGroupService.getAllSemesterGroup();
    }
    @GetMapping("/getAllSemesterGroupIsNonActive")
    public ResponseEntity<?> getAllIsNonActive()
    {
        return this.semesterGroupService.getAllSemesterGroupIsNonActive();
    }
    @GetMapping("/getSemesterGroupById")
    public ResponseEntity<?> getById(@Param("id") String id)
    {
        return this.semesterGroupService.getSemesterGroupById(id);
    }
    @DeleteMapping("/deleteSemesterGroupById")
    public ResponseEntity<?> deleteById(@Param("id") String id)
    {
        return this.semesterGroupService.deleteSemesterGroupById(id);
    }
    @PutMapping("/updateSemesterGroup")
    public ResponseEntity<?> update(@RequestBody SemesterGroupRequest semesterGroupRequest)
    {
        return this.semesterGroupService.updateSemesterGroup(semesterGroupRequest);
    }
}
