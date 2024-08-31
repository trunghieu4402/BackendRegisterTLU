package com.example.ElearningTLU.Controllers.Admin;

import com.example.ElearningTLU.Dto.MajorDto;
import com.example.ElearningTLU.Services.MajorService.MajorServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/major")
public class MajorController {
    @Autowired
    private MajorServiceImp majorServiceImp;
    @PostMapping("/createMajor")
    public ResponseEntity<?> createCategory(@RequestBody MajorDto MajorDto )
    {
        return ResponseEntity.ok(this.majorServiceImp.addMajor(MajorDto));
    }
    @GetMapping("")
    public ResponseEntity<?> getAllMajor()
    {
        return this.majorServiceImp.findAllMajor();
    }
    @GetMapping("/findByMajorId")
    public ResponseEntity<?> getMajorById(@RequestParam("id") String id)
    {
        return this.majorServiceImp.findMajorById(id);
    }
    @DeleteMapping("/deleteMajor")
    public ResponseEntity<?> deleteMajor(@RequestParam("id") String id)
    {
        return this.majorServiceImp.deleteMajor(id);
    }
    @PutMapping("/editMajor")
    public ResponseEntity<?> editMajor(@RequestBody MajorDto majorDto)
    {
        return this.majorServiceImp.editMajor(majorDto);
    }
}
