package com.pensionmaite.pensionmaitebackend.repository;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomTypeRepo extends CrudRepository<RoomType, Integer> {

    Optional<RoomType> findByName(String name);
}
