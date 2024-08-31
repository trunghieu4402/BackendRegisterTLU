package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.CourseGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseGardeRepository extends JpaRepository<CourseGrade,Long>{
}
