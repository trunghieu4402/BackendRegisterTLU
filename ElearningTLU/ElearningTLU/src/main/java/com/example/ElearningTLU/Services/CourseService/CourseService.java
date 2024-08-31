package com.example.ElearningTLU.Services.CourseService;

import com.example.ElearningTLU.Dto.Request.CourseDto;
import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService implements CourseServiceImpl{
    @Autowired
    private StatisticsStudentRepository statisticsStudentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
            private MajorRepository majorRepository;
    @Autowired
            private DepartmentRepository departmentRepository;
//    @Autowired
//            private RequirementRepository requirementRepository;
    @Autowired
            private CourseDepartmentRepository CourseDepartmentRepository;
    @Autowired
            private CourseMajorRepository courseMajorRepository;
    @Autowired
            private CourseSemesterGroupRepository courseSemesterGroupRepository;
    @Autowired
            private SemesterGroupRepository semesterGroupRepository;
    ModelMapper mapper = new ModelMapper();
    public ResponseEntity<?> addCourse(CourseDto courseDto)
    {
        int n=0;
//        System.out.println(courseDto.getCourseId()
//        Optional<Course> course = this.courseRepository.findById(courseDto.getCourseId());
        if(this.courseRepository.findById(courseDto.getCourseId()).isPresent())
        {
            return new ResponseEntity<>("Môn học đã tồn tại", HttpStatus.CONFLICT);
        }
        Course course = new Course();
                course=this.mapper.map(courseDto,Course.class);

        this.courseRepository.save(course);
        StatisticsStudent statisticsStudent = new StatisticsStudent();
        statisticsStudent.setCourse(course);
//        return new ResponseEntity<>(course,HttpStatus.OK);
        if(courseDto.getType()==CourseType.COSO)
        {
            course.setType(CourseType.COSO);
            n+=this.personRepository.findAllPersonByRole(Role.STUDENT.name()).get().size();
            statisticsStudent.setNumberOfStudent(n);
            this.statisticsStudentRepository.save(statisticsStudent);

        }
        else if (courseDto.getType()==CourseType.COSONGANH)
        {
//            Optional<Department> department = this.departmentRepository.findById(courseDto.getDepartmentId());
            if(courseDto.getDepartmentId()==null)
            {
                this.courseRepository.delete(course);
                return new ResponseEntity<>("Vui long Nhap ma Khoa",HttpStatus.BAD_REQUEST);
            }
            for(String id : courseDto.getDepartmentId()) {
                if(this.departmentRepository.findById(id).isEmpty())
                {
                    this.courseRepository.delete(course);
                    return new ResponseEntity<>("Ma Khoa:"+ id +" Khong Ton Tai",HttpStatus.NOT_FOUND);
                }
                Department department = this.departmentRepository.findById(id).get();
                CourseDepartment courseDepartment = new CourseDepartment();
                courseDepartment.setCourse(course);
                courseDepartment.setDepartment(department);
                this.CourseDepartmentRepository.save(courseDepartment);
                if(!this.personRepository.PersonByRoleAndDepartmentId(Role.STUDENT.name(),department.getDepartmentId()).isEmpty())
                {
                    n += this.personRepository.PersonByRoleAndDepartmentId(Role.STUDENT.name(),department.getDepartmentId()).get().size();
                }

            }
            course.setType(CourseType.COSONGANH);
            statisticsStudent.setNumberOfStudent(n);
            this.statisticsStudentRepository.save(statisticsStudent);
        }
        else
        {
            if(courseDto.getMajorId()==null)
            {
                this.courseRepository.delete(course);
                return new ResponseEntity<>("Vui long Nhap ma Nganh",HttpStatus.BAD_REQUEST);
            }
            for(String id:courseDto.getMajorId())
            {
                if(this.majorRepository.findById(id).isEmpty())
                {
                    this.courseRepository.delete(course);
                    return new ResponseEntity<>("Ngành không tồn tại",HttpStatus.NOT_FOUND);
                }

                Major major = this.majorRepository.findById(id).get();
                CourseMajor courseMajor = new CourseMajor();
                courseMajor.setCourse(course);
                courseMajor.setMajor(major);
                this.courseMajorRepository.save(courseMajor);
                if(!this.personRepository.PersonByRoleAndMajorID(Role.STUDENT.name(),major.getMajorId()).isEmpty())
                {
                    n += this.personRepository.PersonByRoleAndMajorID(Role.STUDENT.name(),id).get().size();
                }
            }
            course.setType(CourseType.CHUYENNGANH);
            statisticsStudent.setNumberOfStudent(n);
            this.statisticsStudentRepository.save(statisticsStudent);

        }
        this.courseRepository.save(course);
//        System.out.println("dieu kiejn :"+courseDto.getReqiId());
        if(courseDto.getReqiId()!=null)
        {
            System.out.println("Co dieu kien");
                for(String RequestCourseId: courseDto.getReqiId())
                {
                    if(RequestCourseId.equals(courseDto.getCourseId()))
                    {
                        this.courseRepository.delete(course);
                        return new ResponseEntity<>("Ma Mon "+RequestCourseId+"Khong The La Dieu Kien Cua Chinh no",HttpStatus.BAD_REQUEST);
                    }
                    if(this.courseRepository.findById(RequestCourseId).isEmpty())
                    {
                        this.courseRepository.delete(course);
                        return new ResponseEntity<>("Mã môn Dieu Kien "+RequestCourseId+" không tồn tại",HttpStatus.NOT_FOUND);
                    }
                    System.out.println(course.getCourseId()+"//"+RequestCourseId);
//                    Course requestcourse
                    Course requestcourse=this.courseRepository.findById(RequestCourseId).get();
                    course.getDiemKienTienQuyet().add(requestcourse);
//                    course1=this.courseRepository.findById(courseDto.getCourseId()).get();
                    System.out.println(RequestCourseId);

//                    Requirement requirement = new Requirement();
//                    requirement.setCourse(course);
//                    requirement.setRequestCourse(requestcourse);
//                    Requirement r=this.requirementRepository.save(requirement);
//                    course.getRequestCourse().add(r);
                }
        }
        Course course1=this.courseRepository.save(course);
        return new ResponseEntity<>(course1,HttpStatus.OK);
    }
    public ResponseEntity<?> getAllCourse()
    {
//        List<Course> courseList = new ArrayList<>();
//        for (Course course : this.courseRepository.findAll())
//        {
////            course.setRequestCourse(this.requirementRepository.findRequirementByCourseId(course.getCourseId()).get());
//            course.setListDepartment(this.CourseDepartmentRepository.findCourseDepartmentByCourseId(course.getCourseId()).get());
//            course.setListMajor(this.courseMajorRepository.findCourseMajorByCourseId(course.getCourseId()).get());
//            courseList.add(course);
//        }
        return new ResponseEntity<>(this.courseRepository.findAll(),HttpStatus.OK);
    }
    public ResponseEntity<?> getCourseById(String id)
    {

        if(this.courseRepository.findById(id).isEmpty())
        {
            return new ResponseEntity<>("Môn học không tồn tại",HttpStatus.NOT_FOUND);
        }
        System.out.println("Vao Day R");
        Course course = this.courseRepository.findByCourseId(id);
//        course.setRequestCourse(this.requirementRepository.findRequirementByCourseId(course.getCourseId()).get());
//        course.setListDepartment(this.CourseDepartmentRepository.findCourseDepartmentByCourseId(course.getCourseId()).get());
//        course.setListMajor(this.courseMajorRepository.findCourseMajorByCourseId(course.getCourseId()).get());
//        course= this.getCourse(id);
//        System.out.println(course);

        return new ResponseEntity<>(course,HttpStatus.OK);
    }
    public ResponseEntity<?> updateCourse(CourseDto courseDto)
    {
        int n=0;
        if( this.courseRepository.findById(courseDto.getCourseId()).isEmpty())
        {
            return new ResponseEntity<>("Môn Học Không Tồn Tại",HttpStatus.NOT_FOUND);
        }
        StatisticsStudent statisticsStudent = this.statisticsStudentRepository.findByCourseId(courseDto.getCourseId());
//        CourseStudent courseStudent = new CourseStudent();
//        n = statisticsStudent.getNumberOfStudent();
        Course course = this.courseRepository.findById(courseDto.getCourseId()).get();
        this.clearData(course);
        n=0;
        course = this.mapper.map(courseDto,Course.class);
        if(courseDto.getType()==CourseType.COSO)
        {
                this.courseRepository.save(course);
                n= this.personRepository.findAllPersonByRole(Role.STUDENT.name()).get().size();
                statisticsStudent.setNumberOfStudent(n);
                this.statisticsStudentRepository.save(statisticsStudent);
        }
        else if (courseDto.getType()==CourseType.COSONGANH)
        {
            for (String id:courseDto.getDepartmentId())
            {
                if(this.departmentRepository.findById(id).isEmpty())
                {
                    return new ResponseEntity<>("Ma Khoa"+id+" Khong Ton Tai",HttpStatus.NOT_FOUND);
                }
                Department department = this.departmentRepository.findById(id).get();
                CourseDepartment courseDepartment = new CourseDepartment();
                courseDepartment.setCourse(course);
                courseDepartment.setDepartment(department);
                this.CourseDepartmentRepository.save(courseDepartment);

                if(!this.personRepository.PersonByRoleAndDepartmentId(Role.STUDENT.name(),id).isEmpty())
                {
                    n = this.personRepository.PersonByRoleAndDepartmentId(Role.STUDENT.name(),id).get().size();
                    statisticsStudent.setNumberOfStudent(n);
                }

            }
            this.statisticsStudentRepository.save(statisticsStudent);
        }
        else
        {
            if(courseDto.getMajorId().isEmpty())
            {
                return new ResponseEntity<>("Vui Long Nhap Ma Nganh",HttpStatus.NOT_FOUND);
            }
            for(String i:courseDto.getMajorId())
            {
                if(this.majorRepository.findById(i).isEmpty())
                {
                    return new ResponseEntity<>("Ngành không tồn tại",HttpStatus.NOT_FOUND);
                }
                Major major = this.majorRepository.findById(i).get();
                CourseMajor courseMajor= new CourseMajor();
                courseMajor.setCourse(course);
                courseMajor.setMajor(major);
                this.courseMajorRepository.save(courseMajor);
                course.getListMajor().add(courseMajor);
                if(this.personRepository.PersonByRoleAndMajorID(Role.STUDENT.name(),i).isPresent())
                {
                    n+=this.personRepository.PersonByRoleAndMajorID(Role.STUDENT.name(),i).get().size();
                    statisticsStudent.setNumberOfStudent(n);
                }
//                n+=this.personRepository.findAllPersonByRoleAndDepartment(Role.STUDENT,i).size();
            }
            this.statisticsStudentRepository.save(statisticsStudent);

        }
        this.courseRepository.save(course);
//            System.out.println("Co dieu kien");
            if(courseDto.getReqiId()!=null)
            {
                for(String CourseId: courseDto.getReqiId())
                {
                    Course courseRequest = this.courseRepository.findById(CourseId).get();
                    course.getDiemKienTienQuyet().add(courseRequest);
//                    Requirement requirement = new Requirement();
//                    Course course2 = this.courseRepository.findById(courseDto.getCourseId()).get();
//                    requirement.setRequestCourse(this.courseRepository.findById(CourseId).get());
//                    requirement.setCourse(course2);
//                    this.requirementRepository.save(requirement);
                }

            }

        course=this.courseRepository.save(course);
            course.setStatisticsStudent(statisticsStudent);
            this.updateSemesterGroup(course);
        return new ResponseEntity<>(course,HttpStatus.OK);
    }
    public ResponseEntity<?> deleteCourse(String id)
    {
        Optional<Course> course = this.courseRepository.findById(id);
        if(course.isEmpty())
        {
            return new ResponseEntity<>("Mon hoc khong ton tai",HttpStatus.NOT_FOUND);
        }
        this.clearData(course.get());
        this.courseRepository.delete(course.get());
        return new ResponseEntity<>("OK",HttpStatus.OK);

    }
    public void clearData(Course course)
    {
        CourseType type = course.getType();
        if(type==CourseType.COSO)
        {

        } else if (type==CourseType.COSONGANH) {
            List<CourseDepartment> departments = this.CourseDepartmentRepository.findCourseDepartmentByCourseId(course.getCourseId()).get();
            for (CourseDepartment department : departments) {
                this.CourseDepartmentRepository.delete(department);
            }
        }
        else
        {
            List<CourseMajor> majors = this.courseMajorRepository.findCourseMajorByCourseId(course.getCourseId()).get();
            for (CourseMajor major : majors)
            {
                this.courseMajorRepository.delete(major);
            }
        }
//        if(this.requirementRepository.findRequirementByCourseId(course.getCourseId()).isPresent())
//        {
//            List<Requirement> requirements = this.requirementRepository.findRequirementByCourseId(course.getCourseId()).get();
//            for(Requirement i : requirements)
//            {
//                this.requirementRepository.delete(i);
//            }
//        }
        StatisticsStudent student=this.statisticsStudentRepository.findByCourseId(course.getCourseId());
        this.statisticsStudentRepository.delete(student);
        List<Semester_Group> semesterGroup = this.semesterGroupRepository.findByStatus(false).get();
        for (Semester_Group i : semesterGroup)
        {
            i.getCourseSemesterList().forEach(j->
            {
//                System.out.println(j.getCourseId()+course.getCourseId());
                if(j.getCourseId().equals(course.getCourseId()))
                {
                    this.courseSemesterGroupRepository.delete(j);
                }
            });
        }
    }
    public void addCourse(Course course)
    {
        this.courseRepository.save(course);
    }
    public void updateSemesterGroup(Course course)
    {
        List<Semester_Group> semesterGroup = this.semesterGroupRepository.findByStatus(false).get();
        for (Semester_Group i : semesterGroup)
        {
            i.getCourseSemesterList().forEach(j->
            {
                System.out.println(j.getCourseId()+course.getCourseId());
                if(j.getCourseId().equals(course.getCourseId()))
                {
                    j.setCoefficient(course.getCoefficient());
                    j.setType(course.getType());
                    j.setCredits(course.getCredits());
                    j.setCourseName(course.getCourseName());
                    this.courseSemesterGroupRepository.save(j);
                }
            });
        }
    }
    public ResponseEntity<?> getCourseByMajorId(String id)
    {
        if(this.majorRepository.findById(id).isEmpty())
        {
            return new ResponseEntity<>("Nganh Khong Ton Tai",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.majorRepository.findById(id), HttpStatus.OK);
    }

}
