package com.example.ElearningTLU.Services.SemesterService;

import com.example.ElearningTLU.Repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SemesterService implements SemesterServiceImpl {
    @Autowired
    private SemesterRepository semesterRepository;
    public ResponseEntity<?> getAllSemester()
    {
        return new ResponseEntity<>(this.semesterRepository.findAll(), HttpStatus.OK);
    }
}
