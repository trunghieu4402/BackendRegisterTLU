package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Dto.DepartmentDto;
import com.example.ElearningTLU.Dto.MajorDto;
import com.example.ElearningTLU.Entity.Department;
import com.example.ElearningTLU.Services.DepartmentService.DepartmentServiceImp;
import com.example.ElearningTLU.Services.MajorService.MajorServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Department")
public class DepartmentController {
    @Autowired
    private DepartmentServiceImp departmentServiceImp;

    @GetMapping("")
    public ResponseEntity<?> getAllDepartment()
    {
        return ResponseEntity.ok(this.departmentServiceImp.getAllDepartment());
    }
    @GetMapping("/getDepartmentById")
    public ResponseEntity<?> getDepartmentById(@RequestParam("id") String id)
    {
        return ResponseEntity.ok(this.departmentServiceImp.getDepartmentById(id));
    }
    @PostMapping("/createDepartment")
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto departmentDto )
    {
        System.out.println(departmentDto.getDepartmentId()+"//"+ departmentDto.getDepartmentName());
//        System.out.println("Vao day r");
        return ResponseEntity.ok(this.departmentServiceImp.addDepartment(departmentDto));
    }
    @DeleteMapping("/deleteDepartment")
    public ResponseEntity<?> deleteDepartment(@RequestParam("id") String id)
    {
        return ResponseEntity.ok(this.departmentServiceImp.deleteDepartmentById(id));
    }
    @PutMapping("/editDepartment")
    public ResponseEntity<?> editDepartment(@RequestBody DepartmentDto departmentDto )
    {
        System.out.println(departmentDto.getDepartmentId()+"//"+ departmentDto.getDepartmentName());
//        System.out.println("Vao day r");
        return ResponseEntity.ok(this.departmentServiceImp.editDepartment(departmentDto));
    }


}
