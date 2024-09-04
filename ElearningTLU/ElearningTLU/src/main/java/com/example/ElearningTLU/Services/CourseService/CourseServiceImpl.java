package com.example.ElearningTLU.Services.CourseService;

import com.example.ElearningTLU.Dto.Request.CourseRequest;
import org.springframework.http.ResponseEntity;

public interface CourseServiceImpl {
    public ResponseEntity<?> addCourse(CourseRequest courseRequest);
    public ResponseEntity<?> getAllCourse();
    public ResponseEntity<?> getCourseById(String id);
    public ResponseEntity<?> deleteCourse(String id);
    public ResponseEntity<?> updateCourse(CourseRequest courseRequest);
    public ResponseEntity<?> getCourseByMajorId(String id);
}
