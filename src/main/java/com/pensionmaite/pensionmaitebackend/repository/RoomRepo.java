package com.pensionmaite.pensionmaitebackend.repository;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends CrudRepository<Room, Integer> {
}
