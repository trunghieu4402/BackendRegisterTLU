package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.Course_SemesterGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSemesterGroupRepository extends JpaRepository<Course_SemesterGroup,String> {
    @Query(value = "SELECT * FROM db_e_learningtlu.course_semester_group where semester_group_id =:id",nativeQuery = true)
    Optional<List<Course_SemesterGroup>> findBySemesterGroupId(@Param("id") String id);

    @Query(value = "SELECT * FROM db_e_learningtlu.course_semester_group where semester_group_id =:x or semester_group_id =:y ;", nativeQuery = true)
    List<Course_SemesterGroup> findAllCourseInSemesterGroup(@Param("x") String x, @Param("y") String y);

    @Query(value = "SELECT * FROM db_e_learningtlu.course_semester_group where course_id=:courseId and semester_group_id=:SemesterId ;",nativeQuery = true)
    Course_SemesterGroup findCourseOnSemesterGroup(@Param("courseId") String courseId,@Param("SemesterId") String semesterId);

}
