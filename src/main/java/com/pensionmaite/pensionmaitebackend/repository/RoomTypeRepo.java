package com.pensionmaite.pensionmaitebackend.repository;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepo extends CrudRepository<RoomType, Integer> {

    RoomType findByName(String name);
}
