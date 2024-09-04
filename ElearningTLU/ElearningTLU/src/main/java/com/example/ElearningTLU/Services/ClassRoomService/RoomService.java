package com.example.ElearningTLU.Services.ClassRoomService;

import com.example.ElearningTLU.Dto.Lop;
import com.example.ElearningTLU.Dto.RoomDto;
import com.example.ElearningTLU.Entity.Class;
import com.example.ElearningTLU.Entity.Room;
import com.example.ElearningTLU.Entity.Semester_Group;
import com.example.ElearningTLU.Repository.ClassRepository;
import com.example.ElearningTLU.Repository.RoomRepository;
import com.example.ElearningTLU.Repository.SemesterGroupRepository;
import com.example.ElearningTLU.Utils.RegisterUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService implements RoomServiceImpl{
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private SemesterGroupRepository semesterGroupRepository;
    @Autowired
    private RegisterUtils registerUtils;

    @Autowired
    private ClassRepository classRepository;
    private ModelMapper mapper = new ModelMapper();
    public ResponseEntity<?> getAllRoom()
    {
        return new ResponseEntity<>(this.roomRepository.findAll(), HttpStatus.OK);
    }
    public ResponseEntity<?> getAllRoomBySemester(String id)
    {
        List<RoomDto> roomDtos = new ArrayList<>();
        if(this.semesterGroupRepository.findById(id).isEmpty())
        {
            return new ResponseEntity<>("Ky hoc khong ton tai",HttpStatus.NOT_FOUND);
        }
        roomDtos=this.getAllRoom(id);
        return new ResponseEntity<>(roomDtos,HttpStatus.OK);
    }
    public ResponseEntity<?> getRoomByIdAndSemester(String room, String semester)
    {

        if(this.roomRepository.findById(room).isEmpty())
        {
            return new ResponseEntity<>("Phong "+room+" kohong ton tai",HttpStatus.NOT_FOUND);
        }
        Semester_Group semesterGroupList = this.semesterGroupRepository.findById(semester).get();

            Room room1 = this.roomRepository.findById(room).get();
            return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<RoomDto> getAllRoom(String semesterId)
    {
        List<RoomDto> roomDtos= new ArrayList<>();
        List<Semester_Group> semesterGroupList = this.registerUtils.semesterGroupList(semesterId);
        List<Room> Allroom = this.roomRepository.findAll();
        List<Class> aClass = new ArrayList<>();
        List<String>roomId = new ArrayList<>();
        List<Class> listCourseClass = new ArrayList<>();
        Allroom.forEach(i->
        {
            RoomDto roomDto = this.mapper.map(i,RoomDto.class);
//            roomDto.setSemesterGroupId(semesterGroup.getSemesterGroupId());
            roomDtos.add(roomDto);

        });

        semesterGroupList.forEach(sg ->
        {
            System.out.println(sg.getSemesterGroupId());
            sg.getCourseSemesterList().forEach(course->
            {
                System.out.println(course.getCourseId());
                List<Lop> lopList = new ArrayList<>();
                this.classRepository.getAllClassRoomByCourse(course.getCourseSemesterGroupId()).forEach(x->
                {

                    List<String>t= this.classRepository.getRoomId(course.getCourseSemesterGroupId());
                    roomId.addAll(t);
                    System.out.println("Teacher: "+x.getTeacher().getPersonId());
                    listCourseClass.add(x);
//                    System.out.println(x.getRoom().getRoomId());
                    System.out.println(x.getClassRoomId());
                    aClass.add(x);
                });
            });
        });
        listCourseClass.forEach(i->
        {
            roomDtos.forEach(r->
            {

                if(i.getRoom().getRoomId().equals(r.getRoomId()))
                {
//                    System.out.println(i.getRoom().getRoomId()+"//"+r.getRoomId());
                    Lop lop = new Lop();
                    lop.setSemesterGroup(i.getCourseSemesterGroup().getSemesterGroup().getSemesterGroupId());
                    lop.setClassRoomId(i.getClassRoomId());
                    lop.setStart(i.getStart());
                    lop.setFinish(i.getFinish());
                    if(i.getTeacher() == null)
                    {
                        lop.setTeacherId(null);

                    }
                    lop.setTeacherId(i.getTeacher().getPersonId());


                    r.getLopList().add(lop);
                }
            });
        });
        return roomDtos;
    }

}
