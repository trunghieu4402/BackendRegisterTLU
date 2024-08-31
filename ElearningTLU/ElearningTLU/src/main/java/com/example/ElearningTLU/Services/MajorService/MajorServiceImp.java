package com.example.ElearningTLU.Services.MajorService;

import com.example.ElearningTLU.Dto.MajorDto;
import org.springframework.http.ResponseEntity;


public interface MajorServiceImp {


    ResponseEntity<?> addMajor(MajorDto majorDto);
    public ResponseEntity<?> findAllMajor();
    public ResponseEntity<?> findMajorById(String id);
    public ResponseEntity<?> deleteMajor(String id);
    public ResponseEntity<?> editMajor(MajorDto majorDto);

}
