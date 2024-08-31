package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class GroupStudent {
    @Id
    private String groupId;

    private String groupName;
    private int num;

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    List<Student> studentList = new ArrayList<>();

    @OneToMany(mappedBy = "group")
    @JsonIgnore
    List<Semester_Group> semesterGroups = new ArrayList<>();
}
