package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomTypeRequest;
import com.pensionmaite.pensionmaitebackend.events.response.NewRoomType;

import java.util.Optional;

public interface RoomTypeService {

    NewRoomType createRoomType(CreateRoomTypeRequest createRoomTypeRequest);

    Optional<com.pensionmaite.pensionmaitebackend.entity.RoomType> getRoomTypeByName(String roomType);
}
