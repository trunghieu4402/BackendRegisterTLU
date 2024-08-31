package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.CourseDepartment;
import com.example.ElearningTLU.Entity.CourseMajor;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseMajorRepository extends JpaRepository<CourseMajor,Long> {
    @Query(value = "SELECT * FROM db_e_learningtlu.course_major where course_id =:id",nativeQuery = true)
    Optional<List<CourseMajor>> findCourseMajorByCourseId(@Param("id") String id);
    @Query(value = "SELECT * FROM db_e_learningtlu.course_major where major_id=:id",nativeQuery = true)
    Optional<List<CourseMajor>> findAllCourseMajorByMajorId(@Param("id") String id);
}
