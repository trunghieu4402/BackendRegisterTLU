package com.example.ElearningTLU.Services.SemesterService;

import com.example.ElearningTLU.Dto.SemesterGroupDto;
import org.springframework.http.ResponseEntity;

public interface SemesterGroupServiceImpl {
    public ResponseEntity<?> addSemesterGroup(SemesterGroupDto semesterGroupDto);
    public ResponseEntity<?> getAllSemesterGroup();
    public ResponseEntity<?> getSemesterGroupById(String id);
    public ResponseEntity<?> deleteSemesterGroupById(String id);
    public ResponseEntity<?> updateSemesterGroup(SemesterGroupDto semesterGroupDto);
    public void AutoUpdate();
    public ResponseEntity<?> getAllSemesterGroupIsNonActive();

}
