package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable,Long> {

    @Query(value = "SELECT * FROM db_e_learningtlu.time_table where person_id=:id and semester_group_id=:semesterId ;",nativeQuery = true)
    List<TimeTable> getTimeTableByPersonAndSemesterGroup(@Param("id") String personId,@Param("semesterId") String semesterGroupId);

}
