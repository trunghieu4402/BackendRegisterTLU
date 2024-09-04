package com.example.ElearningTLU.Services.RegisterService;

import com.example.ElearningTLU.Dto.Response.ClassRoomDtoResponse;
import com.example.ElearningTLU.Dto.Response.CourseSemesterGroupResponse;
import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Entity.Class;
import com.example.ElearningTLU.Repository.*;
import com.example.ElearningTLU.Utils.CourseUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    private ClassRepository classRepository;
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
        List<Class> aClasses = new ArrayList<>();
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
            for(Class aClass : courseSemesterGroup.getClassList())
            {
                if(aClass.getClassRoomId().equals(classroom))
                {
                    if(aClass.getRoom().getSeats()== aClass.getCurrentSlot())
                    {
                        return new ResponseEntity<>("Lop Hoc Da Day",HttpStatus.OK);
                    }
                    aClasses.add(aClass);

                }
            }
        }

        if(aClasses.isEmpty())
        {
            return new ResponseEntity<>("Ban Khong duoc Dang Ky Lop: "+classroom,HttpStatus.BAD_REQUEST);
        }
        //kiem tra xem da ddk mon do hay chua
        List<Class> classCurrent = new ArrayList<>();
//        List<ClassRoom_Student> classRoomStudents = new ArrayList<>();
        boolean CheckClass = false;
        // danh sach class sv da dang ky
        List<Class> classList = getAllClassRoomWereRegister(student,semesterGroup);
        for(Class aClass : classList)
        {
            if(aClasses.get(0).getCourseSemesterGroup().getCourseId().equals(aClass.getCourseSemesterGroup().getCourseId()))
            {
                classCurrent.add(aClass);
                this.removeClassRoomStudent(aClass,student);

                aClass.setCurrentSlot(aClass.getCurrentSlot()-1);
                this.classRepository.save(aClass);
                System.out.println("Da Xoa Lop");
                CheckClass=true;
            }
        }
        System.out.println(CheckClass);
        if(!CheckStudentSchedule(aClasses,student,semesterGroup))
        {
            //hoan tra lai class da xoa
            if(CheckClass)
            {
                for(Class aClass : classCurrent)
                {

                    Class_Student roomStudent = new Class_Student();
                    roomStudent.setStudent(student);
                    roomStudent.setAClass(aClass);
                    roomStudent.setMidScore(0L);
                    roomStudent.setEndScore(0L);
                    aClass.setCurrentSlot(aClass.getCurrentSlot()+1);
                    aClass =this.classRepository.save(aClass);
                    this.addStudentToClass(aClass,student);
                    student.getClassRoomStudents().add(roomStudent);
                }
            }
            return new ResponseEntity<>("Thoi Gian Dang Ky Lop Trung",HttpStatus.BAD_REQUEST);
        }
        for (Class aClass : aClasses)
        {
//                    ClassRoom_Student roomStudent = new ClassRoom_Student();
                    aClass.setCurrentSlot(aClass.getCurrentSlot()+1);
                    aClass =this.classRepository.save(aClass);
                    this.addStudentToClass(aClass,student);
        }
        return new ResponseEntity<>("Dang Ky thanh Cong",HttpStatus.OK);
    }
    //Check thoi gian dk hoc
    public boolean CheckStudentSchedule(List<Class> aClasses, Student student, Semester_Group semesterGroup)
    {
        List<Class> classList = this.getAllClassRoomWereRegister(student,semesterGroup);
        System.out.println(classList.size());
//        List<ClassRoom_Student> preSchedules = this.preScheduleRepository.findByStudentIdAndSemesterGroup(student.getPersonId(),semesterGroup);
        if(classList.isEmpty())
        {
            return true;
        }
        for(Class aClass : aClasses)
        {
            System.out.println("Lop Moi: "+ aClass.getClassRoomId()+ aClass.getStart()+"//"+ aClass.getFinish());
            for(Class cl: classList)
            {
            System.out.println("Lop co san: "+cl.getClassRoomId()+cl.getStart()+"//"+cl.getFinish());
                if(aClass.getStart()>=cl.getStart() && aClass.getStart()<=cl.getFinish() || aClass.getFinish()>=cl.getStart() && aClass.getFinish()<=cl.getFinish())
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void addStudentToClass(Class aClass, Student student)
    {
        Class_Student classRoomStudent = new Class_Student();
        classRoomStudent.setAClass(aClass);
        classRoomStudent.setStudent(student);
        classRoomStudent.setMidScore(0L);
        classRoomStudent.setEndScore(0L);
//        classRoomStudent.setStatusCourse(StatusCourse.DANGHOC);

        classRoomStudent=this.classRoomStudentRepository.save(classRoomStudent);
//        student.getClassRoomStudents().add(classRoomStudent);
//        this.personRepository.save(student);
    }
    public void removeClassRoomStudent(Class aClass, Student student)
    {
//        System.out.println("ClassROomId"+classRoom.getClassRoomId());
                Class_Student classRoomStudent= new Class_Student();
               classRoomStudent=this.classRoomStudentRepository.findByClassRoomAndStudent(aClass.getId(),student.getPersonId()).get();
               System.out.println(classRoomStudent.getAClass().getClassRoomId());
               student.getClassRoomStudents().remove(classRoomStudent);
                this.classRoomStudentRepository.delete(classRoomStudent);
       System.out.println("Dax Xoa Sinh vien "+student.getPersonId()+"khoi lop" + aClass.getClassRoomId());
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
        List<Class> classStudents = this.getAllClassRoomWereRegister(student,semesterGroup);
        List<ClassRoomDtoResponse> list=courseUtils.convertToClassRoomResponse(classStudents);
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    public List<Class> getAllClassRoomWereRegister(Student student, Semester_Group semesterGroup)
    {
        Student student1 = this.mapper.map(this.personRepository.findByUserNameOrPersonId(student.getPersonId()),Student.class);;
//        Semester_Group semesterGroup = this.semesterGroupRepository.findSemesterGroupByGroupAndTime(student.getGroup().getGroupId(),"2024-09-10").get();
        List<Class> classStudents = new ArrayList<>();
        for(Class_Student roomStudent: student1.getClassRoomStudents())
        {
            System.out.println(roomStudent.getAClass().getClassRoomId());
            if(roomStudent.getAClass().getCourseSemesterGroup().getSemesterGroup().equals(semesterGroup))
            {
                classStudents.add(roomStudent.getAClass());
            }
        }
        return classStudents;
    }
    //Hủy mon
    public ResponseEntity<?> removeClassRoom(String userId,String classRoomId)
    {
        Student student= this.mapper.map(this.personRepository.findByUserNameOrPersonId(userId).get(),Student.class);
        Semester_Group semesterGroup = this.semesterGroupRepository.findSemesterGroupByGroupAndTime(student.getGroup().getGroupId(),"2024-09-10").get();
//        List<> preSchedule = this.preScheduleRepository.findByStudentIdAndSemesterGroup(student.getPersonId(),semesterGroup.getSemesterGroupId());
        List<Class> aClasses = this.getAllClassRoomWereRegister(student,semesterGroup);
        boolean check= true;
        for (Class aClass : aClasses)
        {
            if(aClass.getClassRoomId().equals(classRoomId))
            {
//                ClassRoom classRoom = schedule.getClassRoom();
                Class_Student classRoomStudent = this.classRoomStudentRepository.findByClassRoomAndStudent(aClass.getId(),student.getPersonId()).get();
                aClass.setCurrentSlot(aClass.getCurrentSlot()-1);
                this.classRepository.save(aClass);
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
