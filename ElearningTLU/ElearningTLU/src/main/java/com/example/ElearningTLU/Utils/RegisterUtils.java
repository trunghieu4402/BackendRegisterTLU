package com.example.ElearningTLU.Utils;

import com.example.ElearningTLU.Entity.ClassRoom;
import com.example.ElearningTLU.Entity.Course_SemesterGroup;
import com.example.ElearningTLU.Entity.Room;
import com.example.ElearningTLU.Entity.Semester_Group;
import com.example.ElearningTLU.Repository.SemesterGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class RegisterUtils {

    @Autowired
    private SemesterGroupRepository semesterGroupRepository;

    public boolean CheckTimeTeacherRegister(String id,int s, int f, String semesterGroupId)
    {
        List<Semester_Group> semesterGroupList = semesterGroupList(semesterGroupId);
        for(Semester_Group semesterGroup: semesterGroupList)
        {
            for(Course_SemesterGroup courseSemesterGroup: semesterGroup.getCourseSemesterList())
            {
                for(ClassRoom classRoom: courseSemesterGroup.getClassRoomList())
                {
                    if(classRoom.getTeacher().getPersonId().equals(id))
                    {
                        if(s>=classRoom.getStart() && s<=classRoom.getFinish() || f>=classRoom.getStart() && f<=classRoom.getFinish())
                        {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public List<Semester_Group> semesterGroupList(String id)
    {
        Semester_Group semesterGroup = this.semesterGroupRepository.findById(id).get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = semesterGroup.getStart().format(formatter);
        String finishDate = semesterGroup.getFinish().format(formatter);
        List<Semester_Group> semesterGroupList = this.semesterGroupRepository.FindSemesterGroupByTime(startDate,finishDate).get();
        return semesterGroupList;
    }
    public boolean CheckTimeForRoom(Room Room, int s, int f, String semesterGroupId)
    {
        List<Semester_Group> semesterGroupList = semesterGroupList(semesterGroupId);
        for(Semester_Group semesterGroup: semesterGroupList)
        {
            for(Course_SemesterGroup courseSemesterGroup: semesterGroup.getCourseSemesterList())
            {
                for(ClassRoom classRoom: courseSemesterGroup.getClassRoomList())
                {
                    if(classRoom.getRoom().equals(Room))
                    {
                        if(s>=classRoom.getStart() && s<=classRoom.getFinish() || f>=classRoom.getStart() && f<=classRoom.getFinish())
                        {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
