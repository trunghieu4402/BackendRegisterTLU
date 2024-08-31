package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.Person;
import com.example.ElearningTLU.Entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person,String> {
    @Query(value = "SELECT * FROM db_e_learningtlu.person where dtype =:role ;",nativeQuery = true)
    Optional<List<Person>> findAllPersonByRole(@Param("role") String role);
    @Query(value = "SELECT * FROM db_e_learningtlu.person where dtype =:role and department_id=:id ;",nativeQuery = true)
//    @Autowired
    Optional<List<Person>> PersonByRoleAndDepartmentId(@Param("role") String role, @Param("id") String id);
    @Query(value = "SELECT * FROM db_e_learningtlu.person where dtype=:role and major_id=:id ;", nativeQuery = true)
//    @Autowired
    Optional<List<Person>> PersonByRoleAndMajorID(@Param("role") String role, @Param("id") String id);
    @Query(value = "SELECT * FROM db_e_learningtlu.person where role =0 ;",nativeQuery = true)
    Optional<Person> findAdmin();
    @Query(value = "SELECT * FROM db_e_learningtlu.person where person_id =:id or user_name =:id ;",nativeQuery = true)
    Optional<Person> findByUserNameOrPersonId(@Param("id") String id);

    @Query(value = "SELECT * FROM db_e_learningtlu.person where dtype =:role and department_id=:departmentId ; ", nativeQuery = true)
    List<Person> findAllByRoleAndDepartment(@Param("role") String r, @Param("departmentId") String department);
}
