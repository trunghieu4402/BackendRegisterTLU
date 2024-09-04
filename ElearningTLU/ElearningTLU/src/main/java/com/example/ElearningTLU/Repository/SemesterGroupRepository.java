package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.Semester_Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SemesterGroupRepository extends JpaRepository<Semester_Group,String> {
    @Query(value = "SELECT * FROM db_e_learningtlu.semester_group where semester_group.active=:i",nativeQuery = true)
    Optional<List<Semester_Group>>getAllSemesterGroupByActive(@Param("i") boolean i);
    @Query(value = "SELECT * FROM db_e_learningtlu.semester_group where active =:status",nativeQuery = true)
    Optional<List<Semester_Group>>findByStatus(@Param("status") boolean s);
    @Query(value = "SELECT * FROM db_e_learningtlu.semester_group where group_id=:group and EXTRACT(YEAR FROM finish) =:year ; ",nativeQuery = true)
    List<Semester_Group> findByGroupAndYear(@Param("group") String groupId , @Param("year") int year);
    @Query(value = "SELECT \n" +
            "    *\n" +
            "FROM\n" +
            "    db_e_learningtlu.semester_group\n" +
            "WHERE\n" +
            "    finish BETWEEN :start AND :finish ;", nativeQuery = true)
    Optional<List<Semester_Group>>FindSemesterGroupByTime(@Param("start") String s,@Param("finish") String f );

    @Query(value = "SELECT * FROM db_e_learningtlu.semester_group where time_dang_ky_hoc =:time ;", nativeQuery = true)
    Optional<Semester_Group>FindSemesterGroupByNowTime(@Param("time") String time);

    @Query(value = "SELECT * FROM db_e_learningtlu.semester_group where group_id=:group and time_dang_ky_hoc=:date",nativeQuery = true)
    Optional<Semester_Group> findSemesterGroupByGroupAndTime(@Param("group") String group,@Param("date") String date);
}
