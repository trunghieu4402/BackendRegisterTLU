package com.example.ElearningTLU.Services.MajorService;

import com.example.ElearningTLU.Dto.MajorDto;
import com.example.ElearningTLU.Entity.Department;
import com.example.ElearningTLU.Entity.Major;
import com.example.ElearningTLU.Repository.DepartmentRepository;
import com.example.ElearningTLU.Repository.MajorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MajorService implements MajorServiceImp {
    private final ModelMapper mapper= new ModelMapper();
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    public ResponseEntity<?> addMajor(MajorDto majorDto)
    {
        if(SearchMajor(majorDto.getMajorId()).isPresent())
        {
            return new ResponseEntity<>("Thong Tin Da Ton Tai",HttpStatus.CONFLICT);
        }

        if(this.departmentRepository.findById(majorDto.getDepartment()).isEmpty())
        {
            return new ResponseEntity<>("Mã Khoa "+majorDto.getDepartment()+"Không tồn tại",HttpStatus.NOT_FOUND);
        }
        Department department = this.departmentRepository.findById(majorDto.getDepartment()).get();
        Major major= new Major();
        major.setMajorId(majorDto.getMajorId());
        major.setMajorName(majorDto.getMajorName());
        major.setDepartment(department);
        major=this.majorRepository.save(major);
//        major = this.majorRepository.findById(major.getMajorId()).get();
//        System.out.println(major);
        return new ResponseEntity<>(major, HttpStatus.OK);
    }
    public ResponseEntity<?> findMajorById(String id)
    {
//        System.out.println("Vao Day r");
        if(this.majorRepository.findById(id).isEmpty())
        {
            return new ResponseEntity<>("Thong Tin Khong Ton Tai",HttpStatus.NOT_FOUND);
        }
//        System.out.println(major.getDepartment());
        Major major = this.majorRepository.findById(id).get();
        System.out.println(major.getCourses().size());
        return new ResponseEntity<>(major,HttpStatus.OK);
    }
    public ResponseEntity<?> findAllMajor()
    {
        return new ResponseEntity<>(this.majorRepository.findAll(),HttpStatus.OK);
    }
    public ResponseEntity<?> deleteMajor(String id)
    {
        if(SearchMajor(id).isEmpty())
        {
            return new ResponseEntity<>("Thong Tin Khong Ton tai", HttpStatus.NOT_FOUND);
        }
        Major major=majorRepository.findById(id).get();
        this.majorRepository.delete(major);
        return new ResponseEntity<>("Xoa Thanh Cong",HttpStatus.OK);
    }
    public ResponseEntity<?> editMajor(MajorDto majorDto)
    {
        if(SearchMajor(majorDto.getMajorId()).isEmpty())
        {
            return new ResponseEntity<>("Thong Tin Khong Ton tai", HttpStatus.NOT_FOUND);
        }
        Major major = this.mapper.map(majorDto,Major.class);
        this.majorRepository.save(major);
        return new ResponseEntity<>("Sua Thanh Cong",HttpStatus.OK);
    }

    public Optional<Major> SearchMajor(String id)
    {
        return this.majorRepository.findById(id);
    }
}
