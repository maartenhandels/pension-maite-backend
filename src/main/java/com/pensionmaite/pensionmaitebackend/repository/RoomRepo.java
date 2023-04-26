package com.pensionmaite.pensionmaitebackend.repository;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoomRepo extends CrudRepository<Room, Integer> {

    @Query("SELECT r FROM Room r WHERE r.roomType.name = :roomType")
    List<Room> findAllByRoomType(String roomType);

    @Query("SELECT r FROM Room r WHERE r.roomType.name IN :roomTypes")
    List<Room> findByCategoryNames(@Param("roomTypes") List<String> roomTypes);
}
