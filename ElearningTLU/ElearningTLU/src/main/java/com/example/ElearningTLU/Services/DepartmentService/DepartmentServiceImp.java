package com.example.ElearningTLU.Services.DepartmentService;

import com.example.ElearningTLU.Dto.DepartmentDto;
import com.example.ElearningTLU.Entity.Department;
import org.springframework.http.ResponseEntity;

public interface DepartmentServiceImp {
    ResponseEntity<?> addDepartment(DepartmentDto departmentDto);
    ResponseEntity<?> getAllDepartment();
    ResponseEntity<?> getDepartmentById(String id);
    ResponseEntity<?> deleteDepartmentById(String id);
    ResponseEntity<?> editDepartment(DepartmentDto departmentDto);
}
