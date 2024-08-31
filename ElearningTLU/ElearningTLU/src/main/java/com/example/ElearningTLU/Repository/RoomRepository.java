package com.example.ElearningTLU.Repository;

import com.example.ElearningTLU.Dto.RoomDto;
import com.example.ElearningTLU.Entity.Lich;
import com.example.ElearningTLU.Entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,String> {
//    List<Room> findAllRoom();
}
