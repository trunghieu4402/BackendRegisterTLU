package com.example.ElearningTLU.Services.SemesterService;

import com.example.ElearningTLU.Dto.SemesterGroupDto;
import com.example.ElearningTLU.Entity.GroupStudent;
import com.example.ElearningTLU.Entity.Semester;
import com.example.ElearningTLU.Entity.Semester_Group;
import com.example.ElearningTLU.Repository.GroupStudentRepository;
import com.example.ElearningTLU.Repository.SemesterGroupRepository;
import com.example.ElearningTLU.Repository.SemesterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class SemesterGroupService implements SemesterGroupServiceImpl{
    @Autowired
    private SemesterRepository semesterRepository;
    @Autowired
    private SemesterGroupRepository semesterGroupRepository;
    @Autowired
    private GroupStudentRepository groupStudentRepository;
    ModelMapper mapper = new ModelMapper();
    public ResponseEntity<?> addSemesterGroup(SemesterGroupDto semesterGroupDto)
    {
        Semester_Group semesterGroup = new Semester_Group();
//        semesterGroup.setSemesterGroupId();
        GroupStudent groupStudent = this.groupStudentRepository.findById(semesterGroupDto.getGroupID()).get();
        Semester semester = this.semesterRepository.findById(semesterGroupDto.getSemesterID()).get();
        LocalDate TimeStart = LocalDate.parse(semesterGroupDto.getStart());
        LocalDate TimeEnd = LocalDate.parse(semesterGroupDto.getEnd());
        LocalDate TimeDK = LocalDate.parse(semesterGroupDto.getTimeDKHoc());
//
        semesterGroup.setSemesterGroupId(semester.getSemesterId()+"_"+groupStudent.getGroupId()+"_"+TimeStart.getYear()+"_"+TimeEnd.getYear());
        semesterGroup.setSemester(semester);
        semesterGroup.setGroup(groupStudent);
        semesterGroup.setStart(TimeStart);
        semesterGroup.setFinish(TimeEnd);
        semesterGroup.setActive(false);
        if(this.semesterGroupRepository.findById(semester.getSemesterId()+"_"+groupStudent.getGroupId()+"_"+TimeStart.getYear()+"_"+TimeEnd.getYear()).isPresent())
        {
            return new ResponseEntity<>("Ky Hoc Da Ton Tai",HttpStatus.CONFLICT);
        }
        if(TimeDK.until(LocalDate.now(),ChronoUnit.DAYS)>=0)
        {
            return new ResponseEntity<>("Thời Gian Mở Kỳ Học Không Phù Hợp", HttpStatus.CONFLICT);
        }
        if(TimeEnd.until(TimeStart,ChronoUnit.DAYS)>=0)
        {
            return new ResponseEntity<>("Thời Gian Mở Kỳ Học Không Phù Hợp", HttpStatus.CONFLICT);
        }


        if(TimeDK.until(TimeStart, ChronoUnit.DAYS)<=0)
        {
            return new ResponseEntity<>("Ngày đăng ký học phải trước ngày bắt đầu kỳ học mới", HttpStatus.CONFLICT);
        }
        semesterGroup.setTimeDangKyHoc(TimeDK);
        semesterGroup.setBaseCost(semesterGroupDto.getBaseCost());
        semesterGroup=this.semesterGroupRepository.save(semesterGroup);
        return new ResponseEntity<>(semesterGroup,HttpStatus.OK);
    }
    public ResponseEntity<?> getAllSemesterGroup()
    {
        return new ResponseEntity<>(this.semesterGroupRepository.findAll(),HttpStatus.OK);
    }
    public ResponseEntity<?> getSemesterGroupById(String id)
    {
        Optional<Semester_Group> semesterGroup = this.semesterGroupRepository.findById(id);
        if(semesterGroup.isEmpty())
        {
            return new ResponseEntity<>("Kỳ học không ton tai",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(semesterGroup.get(),HttpStatus.OK);
    }
    public ResponseEntity<?> deleteSemesterGroupById(String id)
    {
        Optional<Semester_Group> semesterGroup = this.semesterGroupRepository.findById(id);
        if(semesterGroup.isEmpty())
        {
            return new ResponseEntity<>("Kỳ học không ton tai",HttpStatus.NOT_FOUND);
        }
        if(semesterGroup.get().isActive())
        {
             return new ResponseEntity<>("Kỳ này không thể xóa",HttpStatus.BAD_REQUEST);
        }
        this.semesterGroupRepository.delete(semesterGroup.get());
        return new ResponseEntity<>("Xoa Thanh Cong",HttpStatus.OK);
    }
    public ResponseEntity<?> updateSemesterGroup(SemesterGroupDto semesterGroupDto)
    {
        Semester_Group semesterGroup = this.semesterGroupRepository.findById(semesterGroupDto.getSemesterGroupId()).get();
//        semesterGroup.setSemesterGroupId();
        if(semesterGroup.isActive())
        {
//            System.out.println(semesterGroup.getStart().until(LocalDate.now(),ChronoUnit.DAYS));
//            System.out.println(semesterGroup.getTimeDangKyHoc().until(LocalDate.now(),ChronoUnit.DAYS));
            return new ResponseEntity<>("Không the chỉnh sửa kỳ này",HttpStatus.BAD_REQUEST);
        }
        else
        {
//            GroupStudent groupStudent = this.groupStudentRepository.findById(semesterGroupDto.getGroupID()).get();
//            Semester semester = this.semesterRepository.findById(semesterGroupDto.getSemesterID()).get();
            LocalDate TimeStart = LocalDate.parse(semesterGroupDto.getStart());
            LocalDate TimeEnd = LocalDate.parse(semesterGroupDto.getEnd());
            LocalDate TimeDK = LocalDate.parse(semesterGroupDto.getTimeDKHoc());
            if(TimeStart.until(LocalDate.now(),ChronoUnit.DAYS)>0 || TimeEnd.until(TimeStart,ChronoUnit.DAYS)>0)
            {
                return new ResponseEntity<>("Thời Gian Mở Kỳ Học Không Phù Hợp", HttpStatus.CONFLICT);
            }
            if(TimeDK.until(TimeStart, ChronoUnit.DAYS)<0)
            {
                return new ResponseEntity<>("Ngày đăng ký học phải trước ngày bắt đầu kỳ học mới", HttpStatus.CONFLICT);
            }
            if(TimeDK.until(LocalDate.now(),ChronoUnit.DAYS)==0)
            {
                semesterGroup.setActive(true);
            }
            else {
                semesterGroup.setActive(false);
            }
            semesterGroup.setBaseCost(semesterGroupDto.getBaseCost());
            semesterGroup.setStart(LocalDate.parse(semesterGroupDto.getStart()));
            semesterGroup.setFinish(LocalDate.parse(semesterGroupDto.getEnd()));
            semesterGroup.setTimeDangKyHoc(LocalDate.parse(semesterGroupDto.getTimeDKHoc()));
             this.semesterGroupRepository.save(semesterGroup);
             return new ResponseEntity<>("Cap Nhat Thanh cong",HttpStatus.OK);

        }
    }
    public ResponseEntity<?> getAllSemesterGroupIsNonActive()
    {
        return new ResponseEntity<>(this.semesterGroupRepository.getAllSemesterGroupByActive(false),HttpStatus.OK);
    }
    public void AutoUpdate()
    {
        if(this.semesterGroupRepository.getAllSemesterGroupByActive(false).isEmpty())
        {
            return;
        }
        else
        {
            List<Semester_Group> semesterGroupList = this.semesterGroupRepository.getAllSemesterGroupByActive(false).get();
            semesterGroupList.forEach(i->
            {
                if(i.getTimeDangKyHoc().until(LocalDate.now(),ChronoUnit.DAYS)==0)
                {
                    i.setActive(true);
                }
                this.semesterGroupRepository.save(i);
            });

        }

    }

}
