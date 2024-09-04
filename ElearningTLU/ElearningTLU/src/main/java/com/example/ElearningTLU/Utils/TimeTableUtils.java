package com.example.ElearningTLU.Utils;

import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Entity.Class;
import com.example.ElearningTLU.Repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeTableUtils {
    @Autowired
    private TimeTableRepository timeTableRepository;
    public void AutoUpdateTimeTable(Semester_Group semesterGroup)
    {
        for (Course_SemesterGroup courseSemesterGroup : semesterGroup.getCourseSemesterList())
        {
            for (Class aClass : courseSemesterGroup.getClassList())
            {
                TimeTable timeTable = new TimeTable();
                timeTable.setPerson(aClass.getTeacher());
                timeTable.setStart(aClass.getStart());
                timeTable.setEnd(aClass.getFinish());
                timeTable.setClassRoomName(aClass.getName());
                timeTable.setRoomId(aClass.getRoom().getRoomId());
                timeTable.setClassRoomId(aClass.getClassRoomId());
                timeTable.setSemesterGroupId(aClass.getCourseSemesterGroup().getSemesterGroup().getSemesterGroupId());
                timeTable.setTeacherId(aClass.getTeacher().getPersonId());
                this.timeTableRepository.save(timeTable);
            }
        }

    }

    public void AutoUpdateTimeTableForStudent(Semester_Group semesterGroup) {
        for (Course_SemesterGroup courseSemesterGroup : semesterGroup.getCourseSemesterList()) {
            for (Class aClass : courseSemesterGroup.getClassList()) {
                for (Class_Student student : aClass.getClassStudents()) {
                    TimeTable timeTable = new TimeTable();
                    timeTable.setPerson(student.getStudent());
                    timeTable.setStart(aClass.getStart());
                    timeTable.setEnd(aClass.getFinish());
                    timeTable.setClassRoomName(aClass.getName());
                    timeTable.setRoomId(aClass.getRoom().getRoomId());
                    timeTable.setClassRoomId(aClass.getClassRoomId());
                    timeTable.setSemesterGroupId(aClass.getCourseSemesterGroup().getSemesterGroup().getSemesterGroupId());
                    timeTable.setTeacherId(aClass.getTeacher().getPersonId());
                    this.timeTableRepository.save(timeTable);
                }

            }
        }

    }

}
