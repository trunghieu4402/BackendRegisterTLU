package com.example.ElearningTLU.Utils;

import com.example.ElearningTLU.Dto.Response.*;
import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Entity.Class;
import com.example.ElearningTLU.Repository.CourseRepository;
import com.example.ElearningTLU.Repository.MajorRepository;
import com.example.ElearningTLU.Repository.SemesterGroupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseUtils {
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterGroupRepository semesterGroupRepository;


    private ModelMapper mapper= new ModelMapper();


    public List<CourseDtoResponse> getTrainingProgram(String majorId)
    {
        List<CourseDtoResponse> ListCourse= new ArrayList<>();
        Major major = this.majorRepository.findById(majorId).get();
        major.getCourses().forEach(course->
        {
            CourseDtoResponse dto = this.mapper.map(course.getCourse(),CourseDtoResponse.class);
//            dto.setType(course.getCourse().getType().name());
            if(!course.getCourse().getPrerequisites().isEmpty())
            {
                course.getCourse().getPrerequisites().forEach(requirement -> {
                    dto.getReqiId().add(requirement.getCourseId());
//                    System.out.println(requirement.getRequestCourse().getCourseId());
                });
            }
//            dto.setCoefficient();
            ListCourse.add(dto);
        });
        Department department = major.getDepartment();
        department.getCourses().forEach(courseDepartment ->
        {
            CourseDtoResponse dto = this.mapper.map(courseDepartment.getCourse(),CourseDtoResponse.class);
            dto.setType(courseDepartment.getCourse().getType().name());
            if(courseDepartment.getCourse().getPrerequisites()!=null)
            {
                courseDepartment.getCourse().getPrerequisites().forEach(requirement -> {
                    dto.getReqiId().add(requirement.getCourseId());
//                    System.out.println(dto.getCourseId()+"//"+requirement.getCourse().getCourseId());
                });
            }

            ListCourse.add(dto);
        });
        this.courseRepository.findCourseByType(CourseType.COSO).get().forEach(course ->
        {
            CourseDtoResponse dto = this.mapper.map(course,CourseDtoResponse.class);
            dto.setType(course.getType().name());
            if(course.getPrerequisites()!=null)
            {
                course.getPrerequisites().forEach(requirement -> {
                    dto.getReqiId().add(requirement.getCourseId());
//                    System.out.println(dto.getCourseId()+"//"+requirement.getCourse().getCourseId());
                });
            }
            ListCourse.add(dto);
        });
        return ListCourse;
    }
    public List<CourseSemesterGroupResponse>getRegisterCourse(Student student)
    {
        List<CourseSemesterGroupResponse> list= new ArrayList<>();
        LocalDate now = LocalDate.of(2024,9,10);

        //Lay danh sach nhung mon ma SV da hoc
        List<CourseGradeResponse> courseGradeResponses= new ArrayList<>();
                student.getCourseGradeList().forEach(courseGrade ->
        {
            CourseGradeResponse courseGradeResponse = new CourseGradeResponse();
            courseGradeResponse = this.mapper.map(courseGrade,CourseGradeResponse.class);
            courseGradeResponse.setStatus(courseGrade.getStatus().name());
            courseGradeResponses.add(courseGradeResponse);
        });
        Semester_Group semesterGroup =this.semesterGroupRepository.findSemesterGroupByGroupAndTime(student.getGroup().getGroupId(),now.toString()).get();
        List<Course_SemesterGroup> courseSemesterGroups = this.removeCourse(semesterGroup.getCourseSemesterList(),courseGradeResponses,0);
        System.out.println("sze:"+ courseSemesterGroups.size());
        for (Course_SemesterGroup course:courseSemesterGroups)
        {
            CourseSemesterGroupResponse dtoResponse = new CourseSemesterGroupResponse();
            Course course1= this.courseRepository.findByCourseId(course.getCourseId());
//            dtoResponse = this.mapper.map(course,CourseSemesterGroupResponse.class);
            dtoResponse.setCourseId(course.getCourseId());
            dtoResponse.setCourseName(course.getCourseName());
            dtoResponse.setId(course.getCourseSemesterGroupId());
            dtoResponse.setSemesterGroupId(course.getSemesterGroup().getSemesterGroupId());
            if(course1.getType().equals(CourseType.COSO))
            {
                List<ClassRoomDtoResponse> list1 = this.convertToClassRoomResponse(course.getClassList());
                dtoResponse.setClassRoomDtos(list1);
            }
            else if(course1.getType().equals(CourseType.COSONGANH))
            {
                for(CourseDepartment department : course1.getListDepartment())
                {
                    if(department.getDepartment().getDepartmentId().equals(student.getDepartment().getDepartmentId()))
                    {
                        List<ClassRoomDtoResponse> list1 = this.convertToClassRoomResponse(course.getClassList());
                        dtoResponse.setClassRoomDtos(list1);
                        break;
                    }
                }

            }
            else
            {
                for(CourseMajor major : course1.getListMajor())
                {
                    if(major.getMajor().getMajorId().equals(student.getMajor().getMajorId()))
                    {
                        List<ClassRoomDtoResponse> list1 = this.convertToClassRoomResponse(course.getClassList());
                        dtoResponse.setClassRoomDtos(list1);
                        break;
                    }
                }
            }
            list.add(dtoResponse);
        }
        return list;
    }
    //remove All course if Sv were complete or not enough requirement
    public List<Course_SemesterGroup> removeCourse(List<Course_SemesterGroup> courseSemesterGroups, List<CourseGradeResponse> courseGradeResponses,int TC)
    {
        System.out.println("ham Check");
        List<Course_SemesterGroup> list= new ArrayList<>();
        for(Course_SemesterGroup courseSemesterGroup : courseSemesterGroups)
        {
            Course course = this.courseRepository.findByCourseId(courseSemesterGroup.getCourseId());
//            System.out.println("Tins chi:"+course.getRequestCredits()+"//"+TC);
            if(course.getRequestCredits()>TC)
            {
                continue;

            }
            System.out.println("Check:" +courseSemesterGroup.getCourseName());
            boolean check = false;
            for(CourseGradeResponse response : courseGradeResponses)
            {
                System.out.println("Check:" +courseSemesterGroup.getCourseName());
                if(courseSemesterGroup.getCourseId().equals(response.getCourseID()) && response.getStatus().equals("DAT"))
                {
                    check=true;
                    break;
                }
                if(!course.getPrerequisites().isEmpty())
                {
                    for(Course course1: course.getPrerequisites())
                    {
                        if(response.getCourseID().equals(course1.getCourseId()))
                        {
                            check=false;
                            break;
                        }
                        check=true;
                    }
                }

            }
            if(check)
            {
                continue;
            }
            list.add(courseSemesterGroup);
        }
        return list;
    }
    public List<ClassRoomDtoResponse> convertToClassRoomResponse(List<Class> aClasses)
    {
        List<ClassRoomDtoResponse> list = new ArrayList<>();
        for(int i = 0; i< aClasses.size(); i++)
        {
            ClassRoomDtoResponse response = new ClassRoomDtoResponse();
            response.setCurrentSlot(aClasses.get(i).getCurrentSlot());
            response.setClassRoomId(aClasses.get(i).getClassRoomId());
            LichHocResponse lichHoc = new LichHocResponse();
            TeacherResponse teacherResponse = this.mapper.map(aClasses.get(i).getTeacher(),TeacherResponse.class);
            lichHoc.setStart(aClasses.get(i).getStart());
            lichHoc.setFinish(aClasses.get(i).getFinish());
            lichHoc.setRoomId(aClasses.get(i).getRoom().getRoomId());
            lichHoc.setTeacher(teacherResponse);
            response.setMaxSlot(aClasses.get(i).getRoom().getSeats());
            response.getLichHocList().add(lichHoc);
            for(int j = i+1; j< aClasses.size(); j++)
            {
                if(aClasses.get(i).getClassRoomId().equals(aClasses.get(j).getClassRoomId()))
                {
                    LichHocResponse lichHoc1 = new LichHocResponse();
                    TeacherResponse teacherResponse1 = this.mapper.map(aClasses.get(j).getTeacher(),TeacherResponse.class);
                    lichHoc1.setStart(aClasses.get(j).getStart());
                    lichHoc1.setFinish(aClasses.get(j).getFinish());
                    lichHoc1.setRoomId(aClasses.get(j).getRoom().getRoomId());
                    lichHoc1.setTeacher(teacherResponse1);
                    int maxSlot= Math.min(aClasses.get(i).getRoom().getSeats(), aClasses.get(j).getRoom().getSeats());
                    response.setMaxSlot(maxSlot);
                    response.getLichHocList().add(lichHoc1);
                    i+=1;
                }
            }
            list.add(response);
        }
        return list;
    }
}
