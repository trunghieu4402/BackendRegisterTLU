package com.example.ElearningTLU.Services.TeachingScheduleService;

import com.example.ElearningTLU.Dto.Request.GradeStudentRequest;
import org.springframework.http.ResponseEntity;

public interface TeachingScheduleServiceImpl {
    public ResponseEntity<?> getScheduleBySemester(String id, String semester);
    public ResponseEntity<?> getTeacherBySemester(String id);

    public ResponseEntity<?> getStudentListByClassRoom(String TeacherId,String ClassRoomId,String semesterId);

    public ResponseEntity<?> updateStudentScore(String teacherId, GradeStudentRequest gradeStudentRequest);
}
