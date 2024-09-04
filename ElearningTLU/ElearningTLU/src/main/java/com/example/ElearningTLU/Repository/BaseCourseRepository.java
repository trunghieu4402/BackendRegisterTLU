package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.BaseCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseCourseRepository extends JpaRepository<BaseCourse,String> {
}
