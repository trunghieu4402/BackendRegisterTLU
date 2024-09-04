package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.Class;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class,Long> {
    @Query(value = "SELECT distinct class_room_id FROM db_e_learningtlu.class where course_semester_group_id =:id ;",nativeQuery = true)
    Optional<List<String>> findAllByCourseIdVersion(@Param("id") String id);

    @Query(value = "SELECT * FROM db_e_learningtlu.class where room_id=:id ;",nativeQuery = true)
    List<Class> findByRoomId(@Param("id") String roomId);

    @Query(value = "SELECT * FROM db_e_learningtlu.class where class_room_id=:id ;",nativeQuery = true)
    List<Class> findByClassRoomId(@Param("id") String Id);

    @Query(value = "SELECT distinct room_id FROM db_e_learningtlu.class where course_semester_group_id =:id ;", nativeQuery = true )
    List<String> getRoomId(@Param("id") String courseSGId);

    @Query(value = "SELECT * FROM db_e_learningtlu.class  where course_semester_group_id =:id ;", nativeQuery = true)
    List<Class> getAllClassRoomByCourse(@Param("id") String id);

    @Query(value = "SELECT * FROM db_e_learningtlu.class ", nativeQuery = true)
    List<Class> getAllClassRoomByTeacher(@Param("id") String id);

    @Query(value = "SELECT * FROM db_e_learningtlu.class where semester_group_id =:x and room_id=:room or semester_group_id =:y and room_id =:room ORDER BY2 start asc ;", nativeQuery = true)
    List<Class> getAllClassBySemesterId(@Param("x") String id1, @Param("y") String id2, @Param("room") String roomid);
}
