package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.GroupStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupStudentRepository extends JpaRepository<GroupStudent,String> {
    @Query(value = "SELECT * FROM db_e_learningtlu.group_student where group_student.num =:num",nativeQuery = true)
    Optional<GroupStudent> findGroupStudentByNum(@Param("num") int num);

}
