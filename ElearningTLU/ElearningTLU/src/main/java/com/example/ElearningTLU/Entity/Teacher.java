package com.example.ElearningTLU.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Teacher extends Person {
    @OneToOne(mappedBy = "leadDepartment")
    private Department leaddepartment;

    @OneToMany(mappedBy = "teacher")
    List<Class> listClasses = new ArrayList<>();

}
