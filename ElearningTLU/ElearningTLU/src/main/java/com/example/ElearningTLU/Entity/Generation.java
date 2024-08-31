package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Generation {
    @Id
    private String generationId;

    private String generationName;
    private int startYear;

    @OneToMany(mappedBy = "generation")
    @JsonIgnore
    List<Student> studentList = new ArrayList<>();
}
