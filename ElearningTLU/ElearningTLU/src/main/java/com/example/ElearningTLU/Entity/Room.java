package com.example.ElearningTLU.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Room {
    @Id
    private String roomId;
    @Column(nullable = false)
    private String roomName;

    private int seats;

    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
            @JsonIgnoreProperties({"courseSemesterGroup","room"})
    List<Class> classList = new ArrayList<>();
//    @ManyToMany
//    @JoinColumn(name = )
}
