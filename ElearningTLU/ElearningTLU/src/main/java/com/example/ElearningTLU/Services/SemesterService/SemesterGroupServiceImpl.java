package com.example.ElearningTLU.Services.SemesterService;

import com.example.ElearningTLU.Dto.SemesterGroupRequest;
import org.springframework.http.ResponseEntity;

public interface SemesterGroupServiceImpl {
    public ResponseEntity<?> addSemesterGroup(SemesterGroupRequest semesterGroupRequest);
    public ResponseEntity<?> getAllSemesterGroup();
    public ResponseEntity<?> getSemesterGroupById(String id);
    public ResponseEntity<?> deleteSemesterGroupById(String id);
    public ResponseEntity<?> updateSemesterGroup(SemesterGroupRequest semesterGroupRequest);
    public void AutoUpdate();
    public ResponseEntity<?> getAllSemesterGroupIsNonActive();

}
