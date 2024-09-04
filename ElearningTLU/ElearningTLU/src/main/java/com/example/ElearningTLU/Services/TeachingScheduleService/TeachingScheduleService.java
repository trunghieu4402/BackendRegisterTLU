package com.example.ElearningTLU.Services.TeachingScheduleService;

import com.example.ElearningTLU.Dto.Request.GradeStudentRequest;
import com.example.ElearningTLU.Dto.Response.ClassRoomDetailResponse;
import com.example.ElearningTLU.Dto.Response.GradeStudentResponse;
import com.example.ElearningTLU.Dto.TeacherResponse;
import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Entity.Class;
import com.example.ElearningTLU.Repository.*;
import com.example.ElearningTLU.Services.ClassRoomService.RoomService;
import com.example.ElearningTLU.Utils.RegisterUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class TeachingScheduleService implements TeachingScheduleServiceImpl {
    @Autowired
    private TimeTableRepository timeTableRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ClassRoomStudentRepository classRoomStudentRepository;

    @Autowired
    private SemesterGroupRepository semesterGroupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private CourseGardeRepository courseGardeRepository;

    @Autowired
    private StatisticsStudentRepository statisticsStudentRepository;

    @Autowired
    private CourseSemesterGroupRepository courseSemesterGroupRepository;

    @Autowired
    private RegisterUtils registerUtils;
    private ModelMapper mapper = new ModelMapper();


    public ResponseEntity<?> getScheduleBySemester(String id, String semester)
    {
        Person person = this.getPerSon(id);
//        List<TimeTable > teachingSchedules = this.teachingScheduleRepository.findByPersonIdAndSemesterGroupId(person.getPersonId(),semester);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostAuthorize("returnObject.userName== authentication.name")
    public Person getPerSon(String id)
    {
        Person person = this.personRepository.findByUserNameOrPersonId(id).get();
        return person;

    }
    public ResponseEntity<?> getTeacherBySemester(String id)
    {
        List<Semester_Group> semesterGroupList = this.registerUtils.semesterGroupList(id);
        List<TeacherResponse> responseList = new ArrayList<>();
        List<Person> personList = this.personRepository.findAllPersonByRole(Role.TEACHER.name()).get();
        for(Person p: personList)
        {
            TeacherResponse response = new TeacherResponse();
            response = this.mapper.map(p,TeacherResponse.class);
            response.setDepartmentId(p.getDepartment().getDepartmentId());
            responseList.add(response);
        }
        for (Semester_Group semesterGroup : semesterGroupList)
        {
//            for(TeacherResponse res: responseList)
//            {
//                    if(!this.teachingScheduleRepository.findByPersonIdAndSemesterGroupId(res.getPersonId(),semesterGroup.getSemesterGroupId()).isEmpty()) {
//                        List<TeachingSchedule> teachingSchedules = this.teachingScheduleRepository.findByPersonIdAndSemesterGroupId(res.getPersonId(), semesterGroup.getSemesterGroupId());
//                        for(TeachingSchedule schedule : teachingSchedules)
//                        {
//                            ScheduleDto scheduleDto = this.mapper.map(schedule,ScheduleDto.class);
//                            System.out.println(scheduleDto);
//                            res.getTeachingScheduleList().add(scheduleDto);
//                        }
//                    }
//            }
        }
        return new ResponseEntity<>(responseList,HttpStatus.OK);
    }

   public ResponseEntity<?> getStudentListByClassRoom(String TeacherId,String ClassRoomId,String semesterId)
    {
        Person person = this.personRepository.findByUserNameOrPersonId(TeacherId).get();
        Teacher teacher = this.mapper.map(person,Teacher.class);
        LocalDate date = LocalDate.of(2024,9,11);
        List<Class_Student> ListStudent= new ArrayList<>();
        Semester_Group semesterGroup = this.semesterGroupRepository.findById(semesterId).get();
        if(semesterGroup.getTimeDangKyHoc().until(date,ChronoUnit.DAYS)<0)
        {
            return new ResponseEntity<>("Khong Co Thong Tin",HttpStatus.BAD_REQUEST);
        }

            for(Class aClass : teacher.getListClasses())
            {
            if(aClass.getClassRoomId().equals(ClassRoomId)&& aClass.getCourseSemesterGroup().getSemesterGroup().equals(semesterGroup))
                {
                    ClassRoomDetailResponse classRoomDetailResponse= new ClassRoomDetailResponse();
                    classRoomDetailResponse.setClassRoomId(aClass.getClassRoomId());
                    classRoomDetailResponse.setClassRoomName(aClass.getName());
                    classRoomDetailResponse.setSemesterGroupId(aClass.getCourseSemesterGroup().getSemesterGroup().getSemesterGroupId());
                    classRoomDetailResponse.setStart(aClass.getStart());
                    classRoomDetailResponse.setFinish(aClass.getFinish());
    //                List<GradeStudentResponse> studentResponses= new ArrayList<>();
                    for(Class_Student classRoomStudent: aClass.getClassStudents())
                    {
                        GradeStudentResponse studentResponse= new GradeStudentResponse();
                        studentResponse.setStudentId(classRoomStudent.getStudent().getPersonId());
                        studentResponse.setStudentName(classRoomStudent.getStudent().getFullName());
                        studentResponse.setBirthDay(classRoomStudent.getStudent().getDateOfBirth());
                        studentResponse.setMidScore(classRoomStudent.getMidScore());
                        studentResponse.setEndScore(classRoomStudent.getEndScore());
//                        studentResponse.setStatus(StatusCourse.DANGHOC.name());
                        classRoomDetailResponse.getStudentList().add(studentResponse);
                    }
                    return new ResponseEntity<>(classRoomDetailResponse,HttpStatus.OK);

                }
            }
        return new ResponseEntity<>("Khong Co Thong Tin",HttpStatus.BAD_REQUEST);

    }
//update score for Student
    public ResponseEntity<?> updateStudentScore(String id, GradeStudentRequest gradeStudentRequest)
    {
        Semester_Group semesterGroup = this.semesterGroupRepository.findById(gradeStudentRequest.getSemesterId()).get();
        if(LocalDate.now().until(semesterGroup.getFinish(),ChronoUnit.DAYS)<0)
        {
            return new ResponseEntity<>("Khong The Chinh Sua Diem",HttpStatus.BAD_REQUEST);
        }

        Person person = this.personRepository.findByUserNameOrPersonId(id).get();
        Teacher teacher = this.mapper.map(person,Teacher.class);
        List<Class> classList = new ArrayList<>();
        Course course = new Course();
//        this.classRoomRepository.fi
        Student student= this.mapper.map(this.personRepository.findByUserNameOrPersonId(gradeStudentRequest.getStudentId()).get(),Student.class);
        boolean CheckClassRoom= true;
        //lay danh sach lop ma giao vien dang day cuar lop do
        for(Class aClass : teacher.getListClasses())
        {
            if(aClass.getCourseSemesterGroup().getSemesterGroup().equals(semesterGroup) && aClass.getClassRoomId().equals(gradeStudentRequest.getClassRoomId()))
            {
                System.out.println("classRoomId:" + aClass.getClassRoomId());
                course=this.courseRepository.findByCourseId(aClass.getCourseSemesterGroup().getCourseId());
                classList.add(aClass);
                CheckClassRoom=false;
            }
        }
        if (CheckClassRoom)
        {
            return new ResponseEntity<>("Ma Giao Vien "+teacher.getPersonId()+" Khong Duoc Phan Cong Day Lop: "+gradeStudentRequest.getClassRoomId()+" Trong Ky "+semesterGroup.getSemesterGroupId(),HttpStatus.BAD_REQUEST);
        }

//        List<ClassRoom> classRoomList = this.classRoomRepository.findByClassRoomId(classRoom1.getClassRoomId());
        //lay sv theo lop do cap nhat diem cho sv trong lop
        for(Class r: classList)
        {
            if(this.classRoomStudentRepository.findByClassRoomAndStudent(r.getId(),student.getPersonId()).isEmpty())
            {
                return new ResponseEntity<>("Ma Sinh Vien "+gradeStudentRequest.getStudentId()+" Khong Thuoc Lop: "+gradeStudentRequest.getClassRoomId(),HttpStatus.BAD_REQUEST);
            }
            Class_Student classRoomStudent = this.classRoomStudentRepository.findByClassRoomAndStudent(r.getId(),student.getPersonId()).get();
            classRoomStudent.setMidScore(gradeStudentRequest.getMidScore());
            classRoomStudent.setEndScore(gradeStudentRequest.getEndScore());
            this.classRoomStudentRepository.save(classRoomStudent);
        }
        this.UpdateScoreForStudent(student,course,semesterGroup,gradeStudentRequest.getClassRoomId());
        return new ResponseEntity<>("Cap Nha Diem thanh cong",HttpStatus.OK);

    }
    public void UpdateScoreForStudent(Student student, Course course, Semester_Group semesterGroup, String ClassRoomId)
    {
        float DiemGiuaKy=0L;
        float DiemCuoiKy=0L;
        float DiemTb=0L;
        int n=0;
        int credit=student.getTotalCredits();
        Course_SemesterGroup courseSemesterGroup = this.courseSemesterGroupRepository.findCourseOnSemesterGroup(course.getCourseId(),semesterGroup.getSemesterGroupId());
        for (Class cl:courseSemesterGroup.getClassList())
        {
            if(cl.getClassRoomId().equals(ClassRoomId))
            {
                for (Class_Student roomStudent: cl.getClassStudents())
                {
                    if(roomStudent.getStudent().equals(student))
                    {
                        n+=1;
                        DiemGiuaKy+=roomStudent.getMidScore();
                        DiemCuoiKy+=roomStudent.getEndScore();
                    }
                }
            }
        }
        String Stt = null;

        CourseGrade courseGrade = new CourseGrade();
        boolean CheckCourse= false;
//        Course course = this.courseRepository.findByCourseId(classRoom1.getCourseSemesterGroup().getCourseId());
        courseGrade.setCourseID(course.getCourseId());
        courseGrade.setStudent(student);
        courseGrade.setCourseName(course.getCourseName());
        courseGrade.setCredits(course.getCredits());
        //Kiem Tra Xem Sinh Vien da Tung Hoc mon do Chua?
        for(CourseGrade c: student.getCourseGradeList())
        {
            System.out.println(DiemTb);
            DiemTb=DiemTb+(c.getCredits()*c.getFinalScore());
            if(c.getCourseID().equals(course.getCourseId()))
            {
                DiemTb-=(c.getFinalScore()* c.getCredits());
                courseGrade = c;
                CheckCourse=true;
                Stt=c.getStatus().name();

            }
        }
        courseGrade.setMidScore(DiemGiuaKy/n);
        courseGrade.setEndScore(DiemCuoiKy/n);
        courseGrade.setFinalScore(courseGrade.getMidScore()*0.3F+courseGrade.getEndScore()*0.7F);
        if(courseGrade.getMidScore()<4)
        {
            courseGrade.setStatus(StatusCourse.HOCLAI);
            courseGrade.setEndScore(0L);
            courseGrade.setFinalScore(0L);
            student.setTotalCredits(credit);
            if(CheckCourse)
            {
                if(Stt.equals(StatusCourse.Dat.name()))
                {
                    student.setTotalCredits(credit-course.getCredits());
                }
                else
                {
                    student.setTotalCredits(credit);
                }
            }
            
        }
        else
        {
            if(courseGrade.getFinalScore()<4)
            {
                courseGrade.setStatus(StatusCourse.THILAI);
                student.setTotalCredits(credit);
                if(CheckCourse)
                {
                    if(Stt.equals(StatusCourse.Dat.name()))
                    {
                        student.setTotalCredits(credit-course.getCredits());
                    }
                    else
                    {
                        student.setTotalCredits(credit);
                    }
                }
            }
            else
            {
                courseGrade.setStatus(StatusCourse.Dat);
                StatisticsStudent statisticsStudent=this.statisticsStudentRepository.findByCourseId(course.getCourseId());
                statisticsStudent.setNumberOfStudent(statisticsStudent.getNumberOfStudent()-1);
                this.statisticsStudentRepository.save(statisticsStudent);
                student.setTotalCredits(credit+course.getCredits());
                DiemTb+=(courseGrade.getFinalScore()*courseGrade.getCredits());
                if(CheckCourse)
                {
                    if(Stt.equals(StatusCourse.Dat.name()))
                    {
                        student.setTotalCredits(credit);
                    }
                    else
                    {
                        student.setTotalCredits(credit+course.getCredits());
                    }
                }
            }
        }
        System.out.println(DiemTb);
        student.setScore(DiemTb/student.getTotalCredits());
        courseGrade = this.courseGardeRepository.save(courseGrade);
        student.getCourseGradeList().add(courseGrade);
        System.out.println(student.getScore());
        this.personRepository.save(student);
    }
}
