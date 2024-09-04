package com.example.ElearningTLU.Services.UserService;

import com.example.ElearningTLU.Dto.PersonDto;
import com.example.ElearningTLU.Dto.StudentDto;
import com.example.ElearningTLU.Dto.TeacherDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

public interface UserServiceImpl {
    public ResponseEntity<?> CreateStudent(StudentDto personDto);
    public  ResponseEntity<?> GetAllStudent();
    public ResponseEntity<?> GetStudentById(String id);
    public ResponseEntity<?> deleteStudent(String id);
    public ResponseEntity<?> updateStudent(StudentDto studentDto);

    public ResponseEntity<?> createTeacher(TeacherDto dto);

    public ResponseEntity<?> getTeacherById(String id);

    public ResponseEntity<?> getTrainingProgram(String username);

    public ResponseEntity<?> getTimeTableBySemester(String StudentId,String SemesterId);
    public ResponseEntity<?> getTranscript(String StudentId);
}
