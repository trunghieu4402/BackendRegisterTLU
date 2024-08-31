package com.example.ElearningTLU.Services.CourseSemesterGroupService;

import com.example.ElearningTLU.Dto.ClassRoomResponse;
import com.example.ElearningTLU.Dto.Request.ClassRoomDto;
import com.example.ElearningTLU.Dto.Response.ClassRoomDtoResponse;
import com.example.ElearningTLU.Dto.Request.CourseSemesterGroupDto;
import com.example.ElearningTLU.Dto.Response.CourseSemesterGroupResponse;
import com.example.ElearningTLU.Dto.Response.LichHocResponse;
import com.example.ElearningTLU.Dto.Response.TeacherResponse;
import com.example.ElearningTLU.Entity.Course;
import com.example.ElearningTLU.Entity.Course_SemesterGroup;
import com.example.ElearningTLU.Entity.Semester_Group;
import com.example.ElearningTLU.Repository.CourseRepository;
import com.example.ElearningTLU.Repository.CourseSemesterGroupRepository;
import com.example.ElearningTLU.Repository.SemesterGroupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseSemesterGroupService implements CourseSemesterGroupServiceImpl{
    @Autowired
    private CourseSemesterGroupRepository courseSemesterGroupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SemesterGroupRepository semesterGroupRepository;
    private ModelMapper mapper = new ModelMapper();

    public ResponseEntity<?> add(CourseSemesterGroupDto CourseSGDto)
    {
        System.out.println(CourseSGDto.getCourseId());
        System.out.println(CourseSGDto.getSemesterGroupId());
        if(CourseSGDto.getCourseId()==null)
        {
            return new ResponseEntity<>("Vui Long Dien Ma Mon Hoc", HttpStatus.BAD_REQUEST);
        }
        if(CourseSGDto.getSemesterGroupId()==null)
        {
            return new ResponseEntity<>("Vui Long Chon Ky Hoc", HttpStatus.BAD_REQUEST);
        }
        if(this.courseRepository.findById(CourseSGDto.getCourseId()).isEmpty())
        {
            return new ResponseEntity<>("Mon Hoc Khong Ton Tai",HttpStatus.NOT_FOUND);
        }
        if(this.semesterGroupRepository.findById(CourseSGDto.getSemesterGroupId()).isEmpty())
        {
            return new ResponseEntity<>("Ky Hoc Khong Ton tai",HttpStatus.NOT_FOUND);
        }
        Course course = this.courseRepository.findById(CourseSGDto.getCourseId()).get();
        Semester_Group semesterGroup = this.semesterGroupRepository.findById(CourseSGDto.getSemesterGroupId()).get();
        Course_SemesterGroup courseSemesterGroup = this.mapper.map(course,Course_SemesterGroup.class);
        courseSemesterGroup.setCourseSemesterGroupId(course.getCourseId()+"_"+semesterGroup.getSemesterGroupId());
        if(this.courseSemesterGroupRepository.findById(courseSemesterGroup.getCourseSemesterGroupId()).isPresent())
        {
            return new ResponseEntity<>(CourseSGDto.getSemesterGroupId()+"da co mon: "+CourseSGDto.getCourseId(),HttpStatus.CONFLICT);
        }
        courseSemesterGroup.setSemesterGroup(semesterGroup);
        return new ResponseEntity<>(this.courseSemesterGroupRepository.save(courseSemesterGroup),HttpStatus.OK);
    }
    public ResponseEntity<?> getAllBySemesterGroup(String id)
    {
        if(this.semesterGroupRepository.findById(id).isEmpty())
        {
            return new ResponseEntity<>("Ky "+id+" Khong ton tai",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.courseSemesterGroupDtoList(id),HttpStatus.OK);
    }
    public List<CourseSemesterGroupResponse> courseSemesterGroupDtoList(String id)
    {
        List<CourseSemesterGroupResponse> list = new ArrayList<>();
        this.courseSemesterGroupRepository.findBySemesterGroupId(id).get().forEach(courseSemesterGroup ->
        {
            CourseSemesterGroupResponse dto = new CourseSemesterGroupResponse();
            dto.setId(courseSemesterGroup.getCourseSemesterGroupId());
            dto.setCourseId(courseSemesterGroup.getCourseId());
            dto.setSemesterGroupId(courseSemesterGroup.getSemesterGroup().getSemesterGroupId());
            dto.setCourseName(courseSemesterGroup.getCourseName());
            for(int i=0;i<courseSemesterGroup.getClassRoomList().size();i++)
            {
                ClassRoomDtoResponse classRoomDto = new ClassRoomDtoResponse();
                classRoomDto.setClassRoomId(courseSemesterGroup.getClassRoomList().get(i).getClassRoomId());
                LichHocResponse lichHoc = new LichHocResponse();
                TeacherResponse teacherResponse = new TeacherResponse();
                teacherResponse = this.mapper.map(courseSemesterGroup.getClassRoomList().get(i).getTeacher(),TeacherResponse.class);
                lichHoc.setTeacher(teacherResponse);
                lichHoc.setStart(courseSemesterGroup.getClassRoomList().get(i).getStart());
                lichHoc.setFinish(courseSemesterGroup.getClassRoomList().get(i).getFinish());
                lichHoc.setRoomId(courseSemesterGroup.getClassRoomList().get(i).getRoom().getRoomId());
                classRoomDto.getLichHocList().add(lichHoc);
                for(int j=i+1;j<courseSemesterGroup.getClassRoomList().size();j++)
                {

                    if(courseSemesterGroup.getClassRoomList().get(i).getClassRoomId().equals(courseSemesterGroup.getClassRoomList().get(j).getClassRoomId()))
                    {
//                        TeacherResponse teacher = new TeacherResponse();
                        TeacherResponse teacherResponse1 = new TeacherResponse();
                        teacherResponse1 = this.mapper.map(courseSemesterGroup.getClassRoomList().get(j).getTeacher(),TeacherResponse.class);
                        LichHocResponse lichHocResponse = new LichHocResponse();
                        lichHocResponse.setTeacher(teacherResponse1);
                        lichHocResponse.setStart(courseSemesterGroup.getClassRoomList().get(j).getStart());
                        lichHocResponse.setFinish(courseSemesterGroup.getClassRoomList().get(j).getFinish());
                        lichHocResponse.setRoomId(courseSemesterGroup.getClassRoomList().get(j).getRoom().getRoomId());
                        classRoomDto.getLichHocList().add(lichHocResponse);
                        i+=1;
                    }

                }

                dto.getClassRoomDtos().add(classRoomDto);
            }
            list.add(dto);
        });
        return list;
    }
}
