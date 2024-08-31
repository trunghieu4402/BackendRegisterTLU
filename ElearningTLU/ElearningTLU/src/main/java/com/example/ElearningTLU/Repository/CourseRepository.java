package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.Course;
import com.example.ElearningTLU.Entity.CourseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,String>{
//    @Query(value = "SELECT * FROM db_e_learningtlu.course", nativeQuery = true)
    @Autowired
    Optional<List<Course>> findCourseByType(@Param("type")CourseType type);

    @Query(value = "SELECT * FROM db_e_learningtlu.course where course_id =:id ;",nativeQuery = true)
    Course findByCourseId(@Param("id") String id);

}
