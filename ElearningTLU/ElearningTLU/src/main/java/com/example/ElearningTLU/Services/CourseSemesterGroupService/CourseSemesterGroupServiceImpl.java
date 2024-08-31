package com.example.ElearningTLU.Services.CourseSemesterGroupService;

import com.example.ElearningTLU.Dto.Request.CourseSemesterGroupDto;
import org.springframework.http.ResponseEntity;

public interface CourseSemesterGroupServiceImpl {
    public ResponseEntity<?> add(CourseSemesterGroupDto Course);
    public ResponseEntity<?> getAllBySemesterGroup(String id);

}
