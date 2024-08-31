package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester,String> {
}
