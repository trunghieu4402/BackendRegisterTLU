package com.example.ElearningTLU.Services.UserService;

import com.example.ElearningTLU.Dto.*;
import com.example.ElearningTLU.Dto.Response.CourseDtoResponse;
import com.example.ElearningTLU.Dto.Response.CourseGradeResponse;
import com.example.ElearningTLU.Dto.Response.TimeTableResponse;
import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Repository.*;
import com.example.ElearningTLU.Services.EmailService.EmailService;
import com.example.ElearningTLU.Utils.CourseUtils;
import com.example.ElearningTLU.Utils.TimeTableUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserService implements UserServiceImpl{
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseMajorRepository courseMajorRepository;
    @Autowired
    private CourseDepartmentRepository courseDepartmentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private GenerationRepository generationRepository;
    @Autowired
    private GroupStudentRepository groupStudentRepository;

    @Autowired
    private StatisticsStudentRepository statisticsStudentRepository;

    @Autowired
    private CourseUtils courseUtils;
    @Autowired
    private SemesterGroupRepository semesterGroupRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TimeTableUtils timeTableUtils;
    private ModelMapper mapper= new ModelMapper();
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    public ResponseEntity<?> CreateStudent(StudentDto personDto) {
        LocalDate now = LocalDate.now();
        int count = 1;
        if(this.personRepository.findAllPersonByRole(Role.STUDENT.name()).isPresent())
        {
            count+=this.personRepository.findAllPersonByRole(Role.STUDENT.name()).get().size();

        }
        Student student = this.mapper.map(personDto,Student.class);
        student.setPersonId("SVTL"+count);
        student.setRole(Role.STUDENT);
        student.setTotalCredits(0);
        LocalDate date = LocalDate.now();
        student.setStartStudyTime(LocalDate.now());
        Optional<Generation> generation = this.generationRepository.findByStartYear(date.getYear());
        if(generation.isEmpty())
        {
            return new ResponseEntity<>("Khóa Học chưa tồn tại",HttpStatus.NOT_FOUND);
        }
        student.setGeneration(generation.get());
        GroupStudent groupStudent = this.groupStudentRepository.findGroupStudentByNum(1).get();
        student.setGroup(groupStudent);
        if(this.departmentRepository.findById(personDto.getDepartmentId()).isEmpty())
        {
        return new ResponseEntity<>("Ma Khoa"+personDto.getDepartmentId()+" Khong ton tai",HttpStatus.NOT_FOUND);
        }
        if(this.majorRepository.findById(personDto.getMajorId()).isEmpty())
        {
            return new ResponseEntity<>("Ma Nganh"+personDto.getMajorId()+" Khong ton tai",HttpStatus.NOT_FOUND);
        }
        Department department = this.departmentRepository.findById(personDto.getDepartmentId()).get();
        Major major = new Major();
        if(department.getMajorList().isEmpty())
        {
            return new ResponseEntity<>("Khoa "+department.getDepartmentId()+" khong co danh sach Nganh",HttpStatus.BAD_REQUEST);
        }
        for(Major i: department.getMajorList())
        {
            if(i.getMajorId().equals(personDto.getMajorId()))
            {
                major = this.majorRepository.findById(i.getMajorId()).get();
                break;
            }
            major = null;

        }
        if (major==null)
        {
            return new ResponseEntity<>("Trong Khoa "+department.getDepartmentId()+" khong ton tai ma Nganh "+personDto.getMajorId(),HttpStatus.BAD_REQUEST);
        }

        student.setDateOfBirth(LocalDate.parse(personDto.getDateOfBirth()));
        student.setMajor(major);
        student.setDepartment(department);
        student.setScore(0f);
        student.setTotalCredits(0);
        student.setStatus(false);
        student.setUserName("SVTL"+count+"@thanglong.edu.vn");

        student.setPassword(passwordEncoder.encode(student.getPersonId()));
        student.setEmail(personDto.getEmail());
        student = this.personRepository.save(student);
        this.ThemThongKe(student);

        //Them +1 vao so luong sinh vien phai hoc cac mon
        this.sendNotification(student.getUserName(),student.getPersonId(),student.getEmail());
        return new ResponseEntity<>(student, HttpStatus.OK);


    }
    public  ResponseEntity<?> GetAllStudent()
    {
        return new ResponseEntity<>(this.personRepository.findAllPersonByRole(Role.STUDENT.name()),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostAuthorize("returnObject.username==authentication.name")
    public ResponseEntity<?> GetStudentById(String id)
    {
        Optional<Person> student = this.personRepository.findByUserNameOrPersonId(id);
        if(student.isEmpty())
        {
            return new ResponseEntity<>("Sinh Vien Khong Ton Tai",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(student.get(),HttpStatus.OK);
    }
    public ResponseEntity<?> deleteStudent(String id)
    {
        Optional<Person> student = this.personRepository.findById(id);
        if(student.isEmpty())
        {
            return new ResponseEntity<>("Sinh Vien Khong Ton Tai",HttpStatus.NOT_FOUND);
        }
        Student student1 = this.mapper.map(student,Student.class);
        this.XoaThongKe(student1);
        this.personRepository.delete(student.get());
        return new ResponseEntity<>("Xoa Thanh Cong",HttpStatus.OK);

    }
    public ResponseEntity<?> updateStudent(StudentDto studentDto)
    {
        if(this.personRepository.findById(studentDto.getPersonId()).isEmpty())
        {
            return new ResponseEntity<>("Sinh Vien Khong Ton Tai",HttpStatus.NOT_FOUND);
        }
        Person person = this.personRepository.findById(studentDto.getPersonId()).get();
        Student student= this.mapper.map(person,Student.class);


        String username =student.getUserName();
        String password = student.getPassword();

//        Generation generation = this.generationRepository.findById(.getGeneration().getGenerationID()).get();
        LocalDate date = student.getStartStudyTime();
        LocalDate dateEnd = null;
        if(studentDto.isStatus())
        {
             dateEnd= LocalDate.now();
        }
        this.XoaThongKe(student);
        Optional<Generation> generation = this.generationRepository.findByStartYear(date.getYear());

        student= this.mapper.map(studentDto,Student.class);


        student.setDateOfBirth(LocalDate.parse(studentDto.getDateOfBirth()));
        Department department = this.departmentRepository.findById(studentDto.getDepartmentId()).get();
        Major major = this.majorRepository.findById(studentDto.getMajorId()).get();
        student.setDepartment(department);
        student.setFinishStudyTime(dateEnd);
        student.setGeneration(generation.get());
        GroupStudent groupStudent = new GroupStudent();
        LocalDate now= LocalDate.now();
        long n=date.until(now, ChronoUnit.YEARS);
        System.out.println(n);
        if(n<1)
        {
            groupStudent= this.groupStudentRepository.findGroupStudentByNum(1).get();
        }
        else if(n<2)
        {
            groupStudent= this.groupStudentRepository.findGroupStudentByNum(2).get();
        }
        else
        {
            groupStudent=this.groupStudentRepository.findGroupStudentByNum(3).get();
        }
        student.setGroup(groupStudent);

        student.setMajor(major);
        student.setUserName(username);
        student.setPassword(password);
        student.setStartStudyTime(date);
        student.setRole(Role.STUDENT);
        this.personRepository.save(student);
        this.ThemThongKe(student);

        return new ResponseEntity<>(student, HttpStatus.OK);
    }
    public void ThemThongKe(Student student)
    {
        System.out.println("THongKe");
        System.out.println(CourseType.COSO);
        for(Course i :this.courseRepository.findCourseByType(CourseType.COSO).get())
        {
            StatisticsStudent student1 = this.statisticsStudentRepository.findByCourseId(i.getCourseId());
            student1.setNumberOfStudent(student1.getNumberOfStudent()+1);
            this.statisticsStudentRepository.save(student1);
        }
        for(CourseDepartment i: this.courseDepartmentRepository.findAllCourseDepartmentByDepartmentId(student.getDepartment().getDepartmentId()).get())
        {
            StatisticsStudent student1 = this.statisticsStudentRepository.findByCourseId(i.getCourse().getCourseId());
            student1.setNumberOfStudent(student1.getNumberOfStudent()+1);
            this.statisticsStudentRepository.save(student1);
        }
        for(CourseMajor i: this.courseMajorRepository.findAllCourseMajorByMajorId(student.getMajor().getMajorId()).get())
        {
            StatisticsStudent student1 = this.statisticsStudentRepository.findByCourseId(i.getCourse().getCourseId());
            student1.setNumberOfStudent(student1.getNumberOfStudent()+1);
            this.statisticsStudentRepository.save(student1);
        }

    }
    public void XoaThongKe(Student student)
    {
        for(Course i :this.courseRepository.findCourseByType(CourseType.COSO).get())
        {
            StatisticsStudent student1 = this.statisticsStudentRepository.findByCourseId(i.getCourseId());
            student1.setNumberOfStudent(student1.getNumberOfStudent()-1);
            this.statisticsStudentRepository.save(student1);
        }
        for(CourseDepartment i: this.courseDepartmentRepository.findAllCourseDepartmentByDepartmentId(student.getDepartment().getDepartmentId()).get())
        {
            StatisticsStudent student1 = this.statisticsStudentRepository.findByCourseId(i.getCourse().getCourseId());
            student1.setNumberOfStudent(student1.getNumberOfStudent()-1);
            this.statisticsStudentRepository.save(student1);
        }
        for(CourseMajor i: this.courseMajorRepository.findAllCourseMajorByMajorId(student.getMajor().getMajorId()).get())
        {
            StatisticsStudent student1 = this.statisticsStudentRepository.findByCourseId(i.getCourse().getCourseId());
            student1.setNumberOfStudent(student1.getNumberOfStudent()-1);
            this.statisticsStudentRepository.save(student1);
        }

    }
    public void AutoUpdateStudent()
    {
        for(Person i : this.personRepository.findAllPersonByRole(Role.STUDENT.name()).get())
        {
            Student student = this.mapper.map(i,Student.class);
            int n = student.getGroup().getNum();
            GroupStudent groupStudent = new GroupStudent();
            n=n+1;
            if(n==1)
            {
                groupStudent= this.groupStudentRepository.findGroupStudentByNum(1).get();
            }
            else if(n==2)
            {
                groupStudent= this.groupStudentRepository.findGroupStudentByNum(2).get();
            }
            else
            {
                groupStudent=this.groupStudentRepository.findGroupStudentByNum(3).get();
            }
            student.setGroup(groupStudent);
            this.personRepository.save(student);
        }

    }
    public ResponseEntity<?> createTeacher(TeacherDto dto)
    {
        int count = 1;
        if(this.personRepository.findAllPersonByRole(Role.TEACHER.name()).isPresent())
        {
            count+=this.personRepository.findAllPersonByRole(Role.TEACHER.name()).get().size();

        }
        if(this.departmentRepository.findById(dto.getDepartmentId()).isEmpty())
        {
            return new ResponseEntity<>("Khoa "+dto.getDepartmentId()+"Khong Ton tai", HttpStatus.NOT_FOUND);
        }
        Teacher teacher= new Teacher();
        teacher=this.mapper.map(dto,Teacher.class);
        teacher.setPersonId("GV"+count);
        teacher.setRole(Role.TEACHER);
        teacher.setDateOfBirth(LocalDate.parse(dto.getDateOfBirth()));

        teacher.setUserName(teacher.getPersonId()+"@thanglong.edu.vn");
        teacher.setPassword(passwordEncoder.encode(teacher.getPersonId()));
        Department department = this.departmentRepository.findById(dto.getDepartmentId()).get();
        teacher.setDepartment(department);

        teacher=this.personRepository.save(teacher);
        return new ResponseEntity<>(teacher,HttpStatus.OK);

    }
    public ResponseEntity<?> getTeacherById(String id)
    {
        if(this.personRepository.findById(id).isEmpty())
        {
            return new ResponseEntity<>("Mã Giáo Viên: "+id+"Khong Ton Tai",HttpStatus.NOT_FOUND);
        }
        Person person = this.personRepository.findById(id).get();
        return new ResponseEntity<>(person,HttpStatus.OK);

    }
    public ResponseEntity<?> getTrainingProgram(String username)
    {
        if(this.personRepository.findByUserNameOrPersonId(username).isEmpty())
        {
            return new ResponseEntity<>("Sinh Vien Khong Ton Tai",HttpStatus.NOT_FOUND);
        }
        Student student = this.mapper.map(this.personRepository.findByUserNameOrPersonId(username).get(),Student.class);
        Major major = student.getMajor();
        List<CourseDtoResponse> ListCourse= new ArrayList<>();
        ListCourse = this.courseUtils.getTrainingProgram(major.getMajorId());
        return new ResponseEntity<>(ListCourse,HttpStatus.OK);

    }
    public ResponseEntity<?> getTimeTableBySemester(String StudentId,String SemesterId)
    {
        Person person = this.personRepository.findByUserNameOrPersonId(StudentId).get();
        Teacher student = this.mapper.map(person,Teacher.class);
        if(this.semesterGroupRepository.findById(SemesterId).isEmpty())
        {
            return new ResponseEntity<>("Ky Hoc :"+SemesterId+"Khong Ton tai",HttpStatus.BAD_REQUEST);
        }


        Semester_Group semesterGroup= this.semesterGroupRepository.findById(SemesterId).get();
        List<TimeTableResponse> TimeTableResponseList = new ArrayList<>();
//        this.timeTableUtils.AutoUpdateTimeTableForStudent(semesterGroup);
//        this.timeTableUtils.AutoUpdateTimeTable(semesterGroup);

        for(TimeTable timeTable: student.getTimeTableList())
        {
            if(timeTable.getSemesterGroupId().equals(semesterGroup.getSemesterGroupId()))
            {
                TimeTableResponse timeTableResponse = this.mapper.map(timeTable,TimeTableResponse.class);
                TimeTableResponseList.add(timeTableResponse);
            }
        }

        return new ResponseEntity<>(TimeTableResponseList,HttpStatus.OK);
    }
//    public ResponseEntity<?> getScheduleBySemester(String userId,String semesterId)
//    {
//
//
////        List<schedule> scheduleList = new ArrayList<>();
//
//        student.getTimeTables().forEach(TimeTable ->
//        {
//            if(TimeTable.getSemesterGroupId().equals(semesterId))
//            {
//                TimeTableResponse response = this.mapper.map(TimeTable, TimeTableResponse.class);
//                scheduleResponses.add(response);
//            }
//        });
//        if(scheduleResponses.isEmpty())
//        {
//            LocalDate date = LocalDate.of(2024,9,11);
//            if(semesterGroup.getTimeDangKyHoc().until(date,ChronoUnit.DAYS)>0)
//            {
//                List<InvoiceDetail> invoiceDetailList = new ArrayList<>();
//                double Sum =0;
//                for( preSchedule: student.getPreSchedules())
//                {
//
//                    if(preSchedule.getSemesterGroupId().equals(semesterGroup.getSemesterGroupId()))
//                    {
//                        TimeTable TimeTable = new TimeTable();
//                        TimeTable.setClassRoomId(preSchedule.getClassRoom().getClassRoomId());
//                        TimeTable.setPerson(student);
//                        TimeTable.setRoomId(preSchedule.getClassRoom().getRoom().getRoomId());
//                        TimeTable.setClassRoomName(preSchedule.getClassRoom().getName());
//                        TimeTable.setSemesterGroupId(semesterGroup.getSemesterGroupId());
//                        TimeTable.setTeacherId(preSchedule.getClassRoom().getTeacher().getPersonId());
//                        TimeTable.setStart(preSchedule.getClassRoom().getStart());
//                        TimeTable.setEnd(preSchedule.getClassRoom().getFinish());
//                        this.timeTableRepository.save(TimeTable);
//                        // Tao Hoa Don Chi Tiet
//                        String courseId = preSchedule.getClassRoom().getCourseSemesterGroup().getCourseId();
//                        Course course = this.courseRepository.findByCourseId(courseId);
//                        InvoiceDetail invoiceDetail = new InvoiceDetail();
//                        invoiceDetail.setCourseId(course.getCourseId());
//                        invoiceDetail.setCredits(course.getCredits());
//                        invoiceDetail.setCoefficient(course.getCoefficient());
//
//                        invoiceDetail.setBaseCost(semesterGroup.getBaseCost());
//                        invoiceDetail.setTotalCost(invoiceDetail.getBaseCost()*invoiceDetail.getCredits()*invoiceDetail.getCoefficient());
//                        Sum+=invoiceDetail.getTotalCost();
//                        invoiceDetailList.add(invoiceDetail);
//
//                        TimeTableResponse response = this.mapper.map(TimeTable, TimeTableResponse.class);
//                        scheduleResponses.add(response);
//                    }
//                }
//                this.GenerateInvoice(invoiceDetailList,student,semesterGroup);
//
//            }
//        }
//        return new ResponseEntity<>(scheduleResponses,HttpStatus.OK);
//    }

    public void GenerateInvoice(List<InvoiceDetail> invoiceDetailList,Student student,Semester_Group semesterGroup)
    {
        double Sum=0;
        Invoice invoice = new Invoice();
        System.out.println(invoice.getInvoiceId());
        invoice.setStudent(student);
        invoice.setSemesterGroupId(semesterGroup.getSemesterGroupId());
        invoice.setTimePayment(semesterGroup.getTimeDangKyHoc().plus(15,ChronoUnit.DAYS));
        for(InvoiceDetail detail: invoiceDetailList)
        {
            Sum+=detail.getTotalCost();
        }

        invoice.setPaymentStatus(PaymentStatus.CHUATHANHTOAN);
        invoice.setTotalCost(Sum);
        invoice=this.invoiceRepository.save(invoice);
        for(InvoiceDetail detail: invoiceDetailList)
        {
            detail.setInvoice(invoice);
            this.invoiceDetailRepository.save(detail);
        }
    }
    public ResponseEntity<?> getTranscript(String StudentId)
    {
        System.out.println(StudentId);
        List<CourseGradeResponse> list= new ArrayList<>();
        Student student = this.mapper.map(this.personRepository.findByUserNameOrPersonId(StudentId).get(),Student.class);
        List<CourseGrade>courseGrades=student.getCourseGradeList();
        for (CourseGrade grade:courseGrades)
        {
            list.add(this.mapper.map(grade,CourseGradeResponse.class));
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }
    public void sendNotification(String user, String password, String toEmail) {

//            String pass=passwordEncoder.de(password);
            emailResponse response = new emailResponse();
            response.setUsername(user);
            response.setPassword(password);
            this.emailService.sendSimpleEmail(toEmail,"Cung Cap Tai Khoan",response.toString());


    }
}
