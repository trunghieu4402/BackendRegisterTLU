package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.Generation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenerationRepository extends JpaRepository<Generation,String> {
    @Query(value = "SELECT * FROM db_e_learningtlu.generation where generation.start_year=:year",nativeQuery = true)
    public Optional<Generation> findByStartYear(@Param("year") int year);
}
