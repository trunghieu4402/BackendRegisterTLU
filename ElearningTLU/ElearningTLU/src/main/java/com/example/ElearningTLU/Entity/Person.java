package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Person {
    @Id
    protected String personId;

    protected String fullName;
    protected String phoneNumber;
    protected boolean sex;
    protected String email;
    @JsonIgnore
    protected String userName;
    @JsonIgnore
    protected String password;

    protected LocalDate dateOfBirth;
    @ManyToOne
    @JoinColumn(name = "departmentId")
    @JsonIgnoreProperties({"leadDepartment","majorList"})
    protected Department department;
    protected Role role;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    protected List<TimeTable> timeTableList= new ArrayList<>();

}
