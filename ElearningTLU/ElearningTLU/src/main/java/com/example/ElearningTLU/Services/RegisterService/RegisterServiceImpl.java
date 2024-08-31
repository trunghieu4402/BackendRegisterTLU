package com.example.ElearningTLU.Services.RegisterService;

import com.example.ElearningTLU.Dto.Response.ClassRoomDtoResponse;
import com.example.ElearningTLU.Dto.Response.CourseDtoResponse;
import com.example.ElearningTLU.Dto.Response.CourseSemesterGroupResponse;
import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Repository.*;
import com.example.ElearningTLU.Utils.CourseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService{
    @Autowired
    private SemesterGroupRepository semesterGroupRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private ClassRoomRepository classRoomRepository;
    @Autowired
    private CourseUtils courseUtils;
    @Autowired
            private TimeTableRepository timeTableRepository;
    @Autowired
            private ClassRoomStudentRepository classRoomStudentRepository;


    ModelMapper mapper = new ModelMapper();
    // Dang ky hoc
    public ResponseEntity<?> register(String personId, String classroom)
    {
        LocalDate now = LocalDate.now();
        Person person = this.personRepository.findByUserNameOrPersonId(personId).get();
        Student student = this.mapper.map(person,Student.class);
        if(this.semesterGroupRepository.findSemesterGroupByGroupAndTime(student.getGroup().getGroupId(),"2024-09-10").isEmpty())
        {
            return new ResponseEntity<>("Ban khong thuoc doi tuong duoc dang Ky Hoc Ngay Hom Nay",HttpStatus.BAD_REQUEST);
        }

        Semester_Group semesterGroup = this.semesterGroupRepository.findSemesterGroupByGroupAndTime(student.getGroup().getGroupId(),"2024-09-10").get();
        List<ClassRoom> classRooms = new ArrayList<>();
        //Lay Danh Sach ClassRoom trong Ky hien tai trong danh sach nhung mon dc phep dk
        List<CourseSemesterGroupResponse> courseSemesterGroups = this.courseUtils.getRegisterCourse(student);
        List<Course_SemesterGroup> list= new ArrayList<>();
        //Lay danh sach mon duoc mo cua Sv
        for(Course_SemesterGroup courseSemesterGroup : semesterGroup.getCourseSemesterList())
        {
            for (CourseSemesterGroupResponse response: courseSemesterGroups)
            {
                if(courseSemesterGroup.getCourseId().equals(response.getCourseId()))
                {
                    list.add(courseSemesterGroup);
                }
            }

        }
        //lay lop muon dk
        for(Course_SemesterGroup courseSemesterGroup: list )
        {
            for(ClassRoom classRoom : courseSemesterGroup.getClassRoomList())
            {
                if(classRoom.getClassRoomId().equals(classroom))
                {
                    if(classRoom.getRoom().getSeats()==classRoom.getCurrentSlot())
                    {
                        return new ResponseEntity<>("Lop Hoc Da Day",HttpStatus.OK);
                    }
                    classRooms.add(classRoom);

                }
            }
        }

        if(classRooms.isEmpty())
        {
            return new ResponseEntity<>("Ban Khong duoc Dang Ky Lop: "+classroom,HttpStatus.BAD_REQUEST);
        }
        //kiem tra xem da ddk mon do hay chua
        List<ClassRoom> classRoomCurrent= new ArrayList<>();
//        List<ClassRoom_Student> classRoomStudents = new ArrayList<>();
        boolean CheckClass = false;
        // danh sach class sv da dang ky
        List<ClassRoom>classRoomList = getAllClassRoomWereRegister(student,semesterGroup);
        for(ClassRoom classRoom: classRoomList)
        {
            if(classRooms.get(0).getCourseSemesterGroup().getCourseId().equals(classRoom.getCourseSemesterGroup().getCourseId()))
            {
                classRoomCurrent.add(classRoom);
                this.removeClassRoomStudent(classRoom,student);

                classRoom.setCurrentSlot(classRoom.getCurrentSlot()-1);
                this.classRoomRepository.save(classRoom);
                System.out.println("Da Xoa Lop");
                CheckClass=true;
            }
        }
        System.out.println(CheckClass);
        if(!CheckStudentSchedule(classRooms,student,semesterGroup))
        {
            //hoan tra lai class da xoa
            if(CheckClass)
            {
                for(ClassRoom classRoom: classRoomCurrent)
                {

                    ClassRoom_Student roomStudent = new ClassRoom_Student();
                    roomStudent.setStudent(student);
                    roomStudent.setClassRoom(classRoom);
                    roomStudent.setMidScore(0L);
                    roomStudent.setEndScore(0L);
                    classRoom.setCurrentSlot(classRoom.getCurrentSlot()+1);
                    classRoom=this.classRoomRepository.save(classRoom);
                    this.addStudentToClass(classRoom,student);
                    student.getClassRoomStudents().add(roomStudent);
                }
            }
            return new ResponseEntity<>("Thoi Gian Dang Ky Lop Trung",HttpStatus.BAD_REQUEST);
        }
        for (ClassRoom classRoom: classRooms)
        {
//                    ClassRoom_Student roomStudent = new ClassRoom_Student();
                    classRoom.setCurrentSlot(classRoom.getCurrentSlot()+1);
                    classRoom=this.classRoomRepository.save(classRoom);
                    this.addStudentToClass(classRoom,student);
        }
        return new ResponseEntity<>("Dang Ky thanh Cong",HttpStatus.OK);
    }
    //Check thoi gian dk hoc
    public boolean CheckStudentSchedule(List<ClassRoom> classRooms, Student student,Semester_Group semesterGroup)
    {
        List<ClassRoom> classRoomList = this.getAllClassRoomWereRegister(student,semesterGroup);
        System.out.println(classRoomList.size());
//        List<ClassRoom_Student> preSchedules = this.preScheduleRepository.findByStudentIdAndSemesterGroup(student.getPersonId(),semesterGroup);
        if(classRoomList.isEmpty())
        {
            return true;
        }
        for(ClassRoom classRoom: classRooms)
        {
            System.out.println("Lop Moi: "+classRoom.getClassRoomId()+classRoom.getStart()+"//"+classRoom.getFinish());
            for(ClassRoom cl: classRoomList)
            {
            System.out.println("Lop co san: "+cl.getClassRoomId()+cl.getStart()+"//"+cl.getFinish());
                if(classRoom.getStart()>=cl.getStart() && classRoom.getStart()<=cl.getFinish() || classRoom.getFinish()>=cl.getStart() && classRoom.getFinish()<=cl.getFinish())
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void addStudentToClass(ClassRoom classRoom,Student student)
    {
        ClassRoom_Student classRoomStudent = new ClassRoom_Student();
        classRoomStudent.setClassRoom(classRoom);
        classRoomStudent.setStudent(student);
        classRoomStudent.setMidScore(0L);
        classRoomStudent.setEndScore(0L);
//        classRoomStudent.setStatusCourse(StatusCourse.DANGHOC);

        classRoomStudent=this.classRoomStudentRepository.save(classRoomStudent);
//        student.getClassRoomStudents().add(classRoomStudent);
//        this.personRepository.save(student);
    }
    public void removeClassRoomStudent(ClassRoom classRoom,Student student)
    {
//        System.out.println("ClassROomId"+classRoom.getClassRoomId());
                ClassRoom_Student classRoomStudent= new ClassRoom_Student();
               classRoomStudent=this.classRoomStudentRepository.findByClassRoomAndStudent(classRoom.getId(),student.getPersonId()).get();
               System.out.println(classRoomStudent.getClassRoom().getClassRoomId());
               student.getClassRoomStudents().remove(classRoomStudent);
                this.classRoomStudentRepository.delete(classRoomStudent);
       System.out.println("Dax Xoa Sinh vien "+student.getPersonId()+"khoi lop" +classRoom.getClassRoomId());
    }
    public ResponseEntity<?> getAllCLass(String perId)
    {
        Person person = this.personRepository.findByUserNameOrPersonId(perId).get();
        Student student = this.mapper.map(person,Student.class);
        List<CourseSemesterGroupResponse> list= new ArrayList<>();
        list=this.courseUtils.getRegisterCourse(student);
        if (list.isEmpty())
        {
            return new ResponseEntity<>("Ban Chua Toi Thoi Gian Dang Ky",HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    public ResponseEntity<?> getPreSchedule(String userId)
    {
        Student student= this.mapper.map(this.personRepository.findByUserNameOrPersonId(userId).get(),Student.class);
        LocalDate date = LocalDate.now();

        Semester_Group semesterGroup = this.semesterGroupRepository.findSemesterGroupByGroupAndTime(student.getGroup().getGroupId(),"2024-09-10").get();
        List<ClassRoom> classRoomStudents = this.getAllClassRoomWereRegister(student,semesterGroup);
        List<ClassRoomDtoResponse> list=courseUtils.convertToClassRoomResponse(classRoomStudents);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    public List<ClassRoom> getAllClassRoomWereRegister(Student student, Semester_Group semesterGroup)
    {
        Student student1 = this.mapper.map(this.personRepository.findByUserNameOrPersonId(student.getPersonId()),Student.class);;
//        Semester_Group semesterGroup = this.semesterGroupRepository.findSemesterGroupByGroupAndTime(student.getGroup().getGroupId(),"2024-09-10").get();
        List<ClassRoom> classRoomStudents = new ArrayList<>();
        for(ClassRoom_Student roomStudent: student1.getClassRoomStudents())
        {
            System.out.println(roomStudent.getClassRoom().getClassRoomId());
            if(roomStudent.getClassRoom().getCourseSemesterGroup().getSemesterGroup().equals(semesterGroup))
            {
                classRoomStudents.add(roomStudent.getClassRoom());
            }
        }
        return classRoomStudents;
    }
    //Hủy mon
    public ResponseEntity<?> removeClassRoom(String userId,String classRoomId)
    {
        Student student= this.mapper.map(this.personRepository.findByUserNameOrPersonId(userId).get(),Student.class);
        Semester_Group semesterGroup = this.semesterGroupRepository.findSemesterGroupByGroupAndTime(student.getGroup().getGroupId(),"2024-09-10").get();
//        List<> preSchedule = this.preScheduleRepository.findByStudentIdAndSemesterGroup(student.getPersonId(),semesterGroup.getSemesterGroupId());
        List<ClassRoom> classRooms = this.getAllClassRoomWereRegister(student,semesterGroup);
        boolean check= true;
        for (ClassRoom classRoom:classRooms)
        {
            if(classRoom.getClassRoomId().equals(classRoomId))
            {
//                ClassRoom classRoom = schedule.getClassRoom();
                ClassRoom_Student classRoomStudent = this.classRoomStudentRepository.findByClassRoomAndStudent(classRoom.getId(),student.getPersonId()).get();
                classRoom.setCurrentSlot(classRoom.getCurrentSlot()-1);
                this.classRoomRepository.save(classRoom);
                this.classRoomStudentRepository.delete(classRoomStudent);
                check=false;
            }
        }
        if(check)
        {
            return new ResponseEntity<>("Khong the huy mon ma ban chua dang ky",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Huy Mon thanh cong",HttpStatus.OK);
    }
}
