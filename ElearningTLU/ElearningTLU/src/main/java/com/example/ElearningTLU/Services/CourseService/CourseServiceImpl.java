package com.example.ElearningTLU.Services.CourseService;

import com.example.ElearningTLU.Dto.Request.CourseDto;
import org.springframework.http.ResponseEntity;

public interface CourseServiceImpl {
    public ResponseEntity<?> addCourse(CourseDto courseDto);
    public ResponseEntity<?> getAllCourse();
    public ResponseEntity<?> getCourseById(String id);
    public ResponseEntity<?> deleteCourse(String id);
    public ResponseEntity<?> updateCourse(CourseDto courseDto);
    public ResponseEntity<?> getCourseByMajorId(String id);
}
