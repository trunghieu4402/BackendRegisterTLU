package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Dto.Request.CourseRequest;
import com.example.ElearningTLU.Services.CourseService.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/Course")
public class CourseController {
    @Autowired
    private CourseServiceImpl courseService;
    @PostMapping("/addCourse")
   public ResponseEntity<?> addCourse(@RequestBody CourseRequest courseRequest)
    {
        return this.courseService.addCourse(courseRequest);
    }
    @GetMapping("/getAllCourse")
    private ResponseEntity<?> getAll()
    {
        return this.courseService.getAllCourse();
    }
    @GetMapping("/getCourseById")
    private ResponseEntity<?> getById(@Param("id") String id)
    {
        return this.courseService.getCourseById(id);
    }

    @GetMapping("/getCourseByMajor")
    private ResponseEntity<?> getByMajorId(@Param("id") String id)
    {
        return this.courseService.getCourseByMajorId(id);
    }
    @DeleteMapping("/deleteCourse")
    private ResponseEntity<?> deleteById(@Param("id") String id)
    {
        return this.courseService.deleteCourse(id);
    }


    @PutMapping("/updateCourse")
    private ResponseEntity<?> update(@RequestBody CourseRequest courseRequest)
    {
        return this.courseService.updateCourse(courseRequest);
    }

}
