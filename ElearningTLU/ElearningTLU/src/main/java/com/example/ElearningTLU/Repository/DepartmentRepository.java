package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@Repository
//@RepositoryRestResource(collectionResourceRel = "Department", path = "Department")
public interface DepartmentRepository extends JpaRepository<Department,String> {
}
