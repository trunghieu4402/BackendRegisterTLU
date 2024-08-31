package com.example.ElearningTLU.Dto;

import com.example.ElearningTLU.Entity.Department;
import com.example.ElearningTLU.Entity.Role;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonDto {
    protected String PersonId;
    protected String FullName;
    protected String PhoneNumber;
    protected boolean Sex;
    protected String Email;
    protected String DateOfBirth;
}
