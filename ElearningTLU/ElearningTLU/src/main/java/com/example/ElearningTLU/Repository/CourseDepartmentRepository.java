package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.CourseDepartment;
import com.example.ElearningTLU.Entity.CourseMajor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseDepartmentRepository extends JpaRepository<CourseDepartment,Long> {
    @Query(value = "SELECT * FROM db_e_learningtlu.course_department where course_id=:id ;",nativeQuery = true)
    Optional<List<CourseDepartment>>findCourseDepartmentByCourseId(@Param("id") String id);

    @Query(value = "SELECT * FROM db_e_learningtlu.course_department where department_id=:id ;",nativeQuery = true)
    Optional<List<CourseDepartment>> findAllCourseDepartmentByDepartmentId(@Param("id") String id);
}
