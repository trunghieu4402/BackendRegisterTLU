package com.example.ElearningTLU.Utils;

import com.example.ElearningTLU.Entity.Class;
import com.example.ElearningTLU.Entity.Course_SemesterGroup;
import com.example.ElearningTLU.Entity.Room;
import com.example.ElearningTLU.Entity.Semester_Group;
import com.example.ElearningTLU.Repository.ClassRepository;
import com.example.ElearningTLU.Repository.SemesterGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class RegisterUtils {

    @Autowired
    private SemesterGroupRepository semesterGroupRepository;
    @Autowired
    private ClassRepository classRepository;

    public boolean CheckTimeTeacherRegister(String id,int s, int f, String semesterGroupId)
    {
        List<Semester_Group> semesterGroupList = semesterGroupList(semesterGroupId);
        for(Semester_Group semesterGroup: semesterGroupList)
        {
            for(Course_SemesterGroup courseSemesterGroup: semesterGroup.getCourseSemesterList())
            {
                for(Class aClass : courseSemesterGroup.getClassList())
                {
                    if(aClass.getTeacher().getPersonId().equals(id))
                    {
                        if(s>= aClass.getStart() && s<= aClass.getFinish() || f>= aClass.getStart() && f<= aClass.getFinish())
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
//    public boolean CheckTimeForRoom(Room Room, int s, int f, String semesterGroupId)
//    {
//        List<Semester_Group> semesterGroupList = semesterGroupList(semesterGroupId);
//        for(Semester_Group semesterGroup: semesterGroupList)
//        {
//            for(Course_SemesterGroup courseSemesterGroup: semesterGroup.getCourseSemesterList())
//            {
//                for(Class aClass : courseSemesterGroup.getClassList())
//                {
//                    if(aClass.getRoom().equals(Room))
//                    {
//                        if(s>= aClass.getStart() && s<= aClass.getFinish() || f>= aClass.getStart() && f<= aClass.getFinish())
//                        {
//                            return false;
//                        }
//                    }
//                }
//            }
//        }
//        return true;
//    }
    public boolean CheckTimeForRoom(Room Room, int s, int f, String semesterGroupId)
    {
        List<Semester_Group> semesterGroupList = semesterGroupList(semesterGroupId);
        Class aClass = new Class();
        List<Class> list= new ArrayList<>();
        list=this.classRepository.getAllClassBySemesterId(semesterGroupList.get(0).getSemesterGroupId(),semesterGroupList.get(0).getSemesterGroupId(),Room.getRoomId());

        if(semesterGroupList.size()==2)
        {
            list=this.classRepository.getAllClassBySemesterId(semesterGroupList.get(0).getSemesterGroupId(),semesterGroupList.get(1).getSemesterGroupId(),Room.getRoomId());
        }
        System.out.println("empty"+list.isEmpty());
        if(list.isEmpty())
        {
            return true;
        }
        aClass= this.binarySearch(list,s);
        if(aClass.getStart().equals(s))
        {
            return false;
        }
        if(s>=aClass.getStart() && s<=aClass.getFinish() || f>=aClass.getStart() && f<=aClass.getFinish())
        {
            return false;
        }

        return true;
    }
        public Class binarySearch(List<Class> list, int s) {
            int left = 0, right = list.size() - 1;
            Class aClass = new Class();
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (list.get(mid).getStart() == s) {
                    return list.get(mid);
                }
                if (list.get(mid).getStart() < s) {
                    left = mid + 1;
                    aClass = list.get(mid);

                } else {
                    right = mid - 1;
                    aClass = list.get(mid);
                }
            }
            return aClass;
        }
}