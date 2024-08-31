package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "semesterId")

public class Semester {
    @Id
    private String semesterId;
    private String semesterName;
//    private LocalDate Start;
//    private LocalDate End;

  @OneToMany(mappedBy = "semester")
  @JsonIgnore
    List<Semester_Group> groupList = new ArrayList<>();
}
