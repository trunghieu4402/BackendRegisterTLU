package com.example.ElearningTLU.Utils;

import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Repository.TimeTableRepository;
import jakarta.persistence.Table;
import org.jetbrains.annotations.NotNull;
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
            for (ClassRoom classRoom: courseSemesterGroup.getClassRoomList())
            {
                TimeTable timeTable = new TimeTable();
                timeTable.setPerson(classRoom.getTeacher());
                timeTable.setStart(classRoom.getStart());
                timeTable.setEnd(classRoom.getFinish());
                timeTable.setClassRoomName(classRoom.getName());
                timeTable.setRoomId(classRoom.getRoom().getRoomId());
                timeTable.setClassRoomId(classRoom.getClassRoomId());
                timeTable.setSemesterGroupId(classRoom.getCourseSemesterGroup().getSemesterGroup().getSemesterGroupId());
                timeTable.setTeacherId(classRoom.getTeacher().getPersonId());
                this.timeTableRepository.save(timeTable);
            }
        }

    }

    public void AutoUpdateTimeTableForStudent(Semester_Group semesterGroup) {
        for (Course_SemesterGroup courseSemesterGroup : semesterGroup.getCourseSemesterList()) {
            for (ClassRoom classRoom : courseSemesterGroup.getClassRoomList()) {
                for (ClassRoom_Student student : classRoom.getClassRoomStudents()) {
                    TimeTable timeTable = new TimeTable();
                    timeTable.setPerson(student.getStudent());
                    timeTable.setStart(classRoom.getStart());
                    timeTable.setEnd(classRoom.getFinish());
                    timeTable.setClassRoomName(classRoom.getName());
                    timeTable.setRoomId(classRoom.getRoom().getRoomId());
                    timeTable.setClassRoomId(classRoom.getClassRoomId());
                    timeTable.setSemesterGroupId(classRoom.getCourseSemesterGroup().getSemesterGroup().getSemesterGroupId());
                    timeTable.setTeacherId(classRoom.getTeacher().getPersonId());
                    this.timeTableRepository.save(timeTable);
                }

            }
        }

    }

}
