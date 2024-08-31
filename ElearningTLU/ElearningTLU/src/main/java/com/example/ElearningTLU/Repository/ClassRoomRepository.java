package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Entity.ClassRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom,Long> {
    @Query(value = "SELECT distinct class_room_id FROM db_e_learningtlu.class_room where course_semester_group_id =:id ;",nativeQuery = true)
    Optional<List<String>> findAllByCourseIdVersion(@Param("id") String id);

    @Query(value = "SELECT * FROM db_e_learningtlu.class_room where room_id=:id ;",nativeQuery = true)
    List<ClassRoom> findByRoomId(@Param("id") String roomId);

    @Query(value = "SELECT * FROM db_e_learningtlu.class_room where class_room_id=:id ;",nativeQuery = true)
    List<ClassRoom> findByClassRoomId(@Param("id") String Id);

    @Query(value = "SELECT distinct room_id FROM db_e_learningtlu.class_room where course_semester_group_id =:id ;", nativeQuery = true )
    List<String> getRoomId(@Param("id") String courseSGId);

    @Query(value = "SELECT * FROM db_e_learningtlu.class_room  where course_semester_group_id =:id ;", nativeQuery = true)
    List<ClassRoom> getAllClassRoomByCourse(@Param("id") String id);

    @Query(value = "SELECT * FROM db_e_learningtlu.class_room ", nativeQuery = true)
    List<ClassRoom> getAllClassRoomByTeacher(@Param("id") String id);
}
