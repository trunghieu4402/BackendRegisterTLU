package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.StatisticsStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsStudentRepository extends JpaRepository<StatisticsStudent,Long> {
    @Query(value = "SELECT * FROM db_e_learningtlu.statistics_student where course_id =:id",nativeQuery = true)
    StatisticsStudent findByCourseId(@Param("id") String id);
}
