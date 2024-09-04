package com.example.ElearningTLU.Services.ClassRoomService;

import com.example.ElearningTLU.Dto.*;
import com.example.ElearningTLU.Dto.Request.ClassRequest;
import com.example.ElearningTLU.Dto.Request.LichHocRequest;
import com.example.ElearningTLU.Dto.Response.ClassRoomDtoResponse;
import com.example.ElearningTLU.Dto.Response.CourseSemesterGroupResponse;
import com.example.ElearningTLU.Dto.Response.LichHocResponse;
import com.example.ElearningTLU.Dto.Response.TeacherResponse;
import com.example.ElearningTLU.Entity.*;
import com.example.ElearningTLU.Entity.Class;
import com.example.ElearningTLU.Repository.*;
import com.example.ElearningTLU.Services.CourseSemesterGroupService.CourseSemesterGroupService;
import com.example.ElearningTLU.Utils.RegisterUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ClassRoomService implements ClassRoomServiceImpl{
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private CourseSemesterGroupRepository courseSemesterGroupRepository;
    ModelMapper mapper = new ModelMapper();
    @Autowired
    private RoomService roomService;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private SemesterGroupRepository semesterGroup;

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private CourseSemesterGroupService courseSemesterGroupService;

    @Autowired
    private RegisterUtils registerUtils;
    public ResponseEntity<?> createClassRoom(ClassRequest classRequest)
    {
        int n=0;
        CourseSemesterGroupResponse response = new CourseSemesterGroupResponse();
        List<Class> classList = new ArrayList<>();
        n = this.classRepository.findAllByCourseIdVersion(classRequest.getCourseSemesterGroupId()).get().size();
        System.out.println(n);
        for(int i = 0; i< classRequest.getLichHocRequestList().size()-1; i++)
        {
            for(int j = i+1; j< classRequest.getLichHocRequestList().size(); j++)
            {
                if(classRequest.getLichHocRequestList().get(i).getStart()>= classRequest.getLichHocRequestList().get(j).getStart() && classRequest.getLichHocRequestList().get(i).getStart()<= classRequest.getLichHocRequestList().get(j).getFinish() ||
                        classRequest.getLichHocRequestList().get(i).getFinish()>= classRequest.getLichHocRequestList().get(j).getStart() && classRequest.getLichHocRequestList().get(i).getFinish()<= classRequest.getLichHocRequestList().get(j).getFinish())
                {
                    return new ResponseEntity<>("Lịch Học Của 1 lớp ko dc trùng thời gian",HttpStatus.BAD_REQUEST);
                }
            }
        }
        if(this.courseSemesterGroupRepository.findById(classRequest.getCourseSemesterGroupId()).isEmpty())
        {
            return new ResponseEntity<>("Học phần "+ classRequest.getCourseSemesterGroupId()+" Không được mở",HttpStatus.NOT_FOUND);
        }
        List<Integer> seat = new ArrayList<>();
        n+=1;
        Course_SemesterGroup courseSemesterGroup = this.courseSemesterGroupRepository.findById(classRequest.getCourseSemesterGroupId()).get();
        for(LichHocRequest lichHocRequest : classRequest.getLichHocRequestList()) {
            if (!CheckTime(lichHocRequest.getStart(), lichHocRequest.getFinish())) {
                return new ResponseEntity<>("Thoi Gian mo Lop khong hop Ly", HttpStatus.BAD_REQUEST);
            }
            if (!this.registerUtils.CheckTimeTeacherRegister(lichHocRequest.getTeacher(), lichHocRequest.getStart(), lichHocRequest.getFinish(), courseSemesterGroup.getSemesterGroup().getSemesterGroupId())) {
                return new ResponseEntity<>("Giao Vien bi trùng lịch dạy", HttpStatus.CONFLICT);
            }
            Room room = this.roomRepository.findById(lichHocRequest.getRoomId()).get();
            if (!this.registerUtils.CheckTimeForRoom(room, lichHocRequest.getStart(), lichHocRequest.getFinish(), courseSemesterGroup.getSemesterGroup().getSemesterGroupId())) {
                return new ResponseEntity<>("Thoi Gian Dang Ky Hoc O Phong: " + lichHocRequest.getRoomId() + "bi trung", HttpStatus.BAD_REQUEST);
            }
            Person person = this.personRepository.findById(lichHocRequest.getTeacher()).get();
            Teacher teacher = this.mapper.map(person, Teacher.class);
            Class aClass = new Class();

            aClass.setClassRoomId(courseSemesterGroup.getCourseId() + "." + n);
            aClass.setName(courseSemesterGroup.getCourseName() + "." + n);

            aClass.setCourseSemesterGroup(courseSemesterGroup);
            aClass.setRoom(room);
//            classRoom.set
            aClass.setStart(lichHocRequest.getStart());
            aClass.setFinish(lichHocRequest.getFinish());
            aClass.setTeacher(teacher);
            aClass.setSemesterGroupId(courseSemesterGroup.getSemesterGroup().getSemesterGroupId());
            aClass =this.classRepository.save(aClass);
            seat.add(aClass.getRoom().getSeats());
            classList.add(aClass);
        }
        int min =0;
        for(int i=0;i<seat.size();i++)
        {
            min = seat.get(i);
            for (int j=i;j<seat.size();j++)
            {
                if(min>seat.get(j))
                {
                    min=seat.get(j);
                }
            }
        }
        ClassRoomDtoResponse classRoomDtoResponse = new ClassRoomDtoResponse();
        for(Class aClass : classList)
        {

            classRoomDtoResponse.setClassRoomId(aClass.getClassRoomId());
            classRoomDtoResponse.setCurrentSlot(aClass.getCurrentSlot());
            classRoomDtoResponse.setMaxSlot(min);
            LichHocResponse lichHocResponse = new LichHocResponse();
            TeacherResponse teacherResponse = new TeacherResponse();
            teacherResponse.setPersonId(aClass.getTeacher().getPersonId());
            teacherResponse.setFullName(aClass.getTeacher().getFullName());
            teacherResponse.setPhoneNumber(aClass.getTeacher().getPhoneNumber());
            lichHocResponse.setTeacher(teacherResponse);
            lichHocResponse.setRoomId(aClass.getRoom().getRoomId());
            lichHocResponse.setStart(aClass.getStart());
            lichHocResponse.setFinish(aClass.getFinish());
            classRoomDtoResponse.getLichHocList().add(lichHocResponse);
        }
        return new ResponseEntity<>(classRoomDtoResponse,HttpStatus.OK);
    }
    public boolean CheckThongtin(List<LichHocRequest> lichHocRequestList)
    {
        for(LichHocRequest lichHocRequest : lichHocRequestList)
        {
            if(this.roomRepository.findById(lichHocRequest.getRoomId()).isEmpty()||this.personRepository.findById(lichHocRequest.getTeacher()).isEmpty())
            {
                return false;
            }
        }
        return true;
    }
    public ResponseEntity<?> autoAddClassRoom(AutoAddClassRoom classRoom)
    {
        List<ClassRoomResponse> classRoomResponses= new ArrayList<>();
        int verson=0;
        if(this.classRepository.findAllByCourseIdVersion(classRoom.getCourseSemesterGroupId()).get().size()!=0)
        {
            verson=this.classRepository.findAllByCourseIdVersion(classRoom.getCourseSemesterGroupId()).get().size();
            System.out.println(verson);
        }

        Course_SemesterGroup courseSemesterGroup = this.courseSemesterGroupRepository.findById(classRoom.getCourseSemesterGroupId()).get();
        for(int i=0;i<classRoom.getStart().size();i++)
        {
            if(!CheckTime(classRoom.getStart().get(i),classRoom.getFinish().get(i)))
            {
                return new ResponseEntity<>("Thoi Gian Mo lop khong hop ly",HttpStatus.BAD_REQUEST);
            }
            List<RoomDto> roomDtoList = this.roomService.getAllRoom(courseSemesterGroup.getSemesterGroup().getSemesterGroupId());
            int v= verson;
            int start= classRoom.getStart().get(i);
            int finish = classRoom.getFinish().get(i);
            this.checkOverlap(roomDtoList, 0, start, finish, classRoom.getCountClass(),v+1,courseSemesterGroup);
        }

        return new ResponseEntity<>(this.getAllClassroomByCourseId(classRoom.getCourseSemesterGroupId()),HttpStatus.OK);
    }
    public List<Teacher> teacherList (int start,int finish, Course course)
    {
        List<Teacher> list = new ArrayList<>();
        for(CourseDepartment department : course.getListDepartment())
        {
            this.personRepository.findAllByRoleAndDepartment(Role.TEACHER.name(),department.getDepartment().getDepartmentId()).forEach(i->
            {
                Teacher teacher = this.mapper.map(i,Teacher.class);
                list.add(teacher);
            });
        }

        return list;
//
    }
    public void checkOverlap(List<RoomDto> roomDtoList, int s, int start, int finish, int n, int verson, Course_SemesterGroup courseSemesterGroup) {
        if (n==0) {
            return;
        }

        RoomDto room = roomDtoList.get(s);
        boolean overlap = room.getLopList().stream().anyMatch(lop ->
                (finish >= lop.getStart() && finish <= lop.getFinish()) ||
                        (start >= lop.getStart() && start <= lop.getFinish())
        );

        if (!overlap) {
            Lop lop1 = new Lop();
                        lop1.setStart(start);
                        lop1.setFinish(finish);
                        lop1.setClassRoomId(courseSemesterGroup.getCourseId()+" ."+verson);
                        lop1.setSemesterGroup(courseSemesterGroup.getSemesterGroup().getSemesterGroupId());
                        room.getLopList().add(lop1);

                        Class class1 = new Class();
                        class1.setRoom(this.roomRepository.findById(room.getRoomId()).get());
                        class1.setName(courseSemesterGroup.getCourseName()+" ."+verson);
                        class1.setClassRoomId(courseSemesterGroup.getCourseId()+"." +verson);
                        class1.setStart(lop1.getStart());
                        class1.setFinish(lop1.getFinish());
                        class1.setCurrentSlot(0);
                        class1.setCourseSemesterGroup(courseSemesterGroup);
                        class1 =this.classRepository.save(class1);
                        verson+=1;
                        n-=1;
//                        classRoomList.add(classRoom1);

            // Thực hiện hành động khi không có sự trùng lặp

        }

        checkOverlap(roomDtoList, s + 1, start, finish, n, verson, courseSemesterGroup);
    }

    public ResponseEntity<?> getAllClassroomByCourseId(String id)
    {
        List<ClassRoomResponse> classRoomResponses = new ArrayList<>();
        Course_SemesterGroup course=this.courseSemesterGroupRepository.findById(id).get();

        for (int i = 0; i<course.getClassList().size(); i++)
        {
            boolean shouldBreak = false;
            for (ClassRoomResponse response : classRoomResponses)
            {
                if(course.getClassList().get(i).getClassRoomId().equals(response.getClassRoomId()))
                {
                    shouldBreak = true;
                    break;
                }
            }
            if(shouldBreak)
            {
                continue;
            }
            ClassRoomResponse classRoomResponse = new ClassRoomResponse();
            classRoomResponse.setClassRoomId(course.getClassList().get(i).getClassRoomId());
            List<LichHocRequest> lichHocRequestList = new ArrayList<>();
            for (int j = i; j<course.getClassList().size(); j++)
            {
                if(course.getClassList().get(i).getClassRoomId().equals(course.getClassList().get(j).getClassRoomId()))
                {
                    LichHocRequest lichHocRequest1 = new LichHocRequest();
                    lichHocRequest1.setRoomId(course.getClassList().get(j).getRoom().getRoomId());
                    lichHocRequest1.setStart(course.getClassList().get(j).getStart());
                    lichHocRequest1.setFinish(course.getClassList().get(j).getFinish());
                    lichHocRequest1.setTeacher(course.getClassList().get(j).getTeacher().getPersonId());
                    lichHocRequestList.add(lichHocRequest1);

                }
            }
            classRoomResponse.setClassRoomName(course.getClassList().get(i).getName());
            classRoomResponse.setLichHocRequestList(lichHocRequestList);
            classRoomResponses.add(classRoomResponse);
        }

        return new ResponseEntity<>(classRoomResponses,HttpStatus.OK);
    }
    public ResponseEntity<?> getAllClassRoomBySemester(String id)
    {
        List<Class> classList = new ArrayList<>();
        if(this.semesterGroup.findById(id).isEmpty())
        {
            return new ResponseEntity<>("Ky hoc "+id+" Khong Ton tai",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(this.courseSemesterGroupService.courseSemesterGroupDtoList(id),HttpStatus.OK);
    }
    public boolean CheckTime(int start, int finish)
    {
        if(finish<start)
        {
            return false;
        }
        int s= start/5;
        int f= finish/5;
        System.out.println(s+"//"+f);

        if(s!=f)
        {
            return false;
        }
        if(finish>60||start<0)
        {
            return false;
        }
        return true;
    }
    public ResponseEntity<?> getClassRoom(String id)
    {
        List<Class> classList = this.classRepository.findByClassRoomId(id);
        return new ResponseEntity<>(classList,HttpStatus.OK);
    }

}
