package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomTypeRequest;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomTypeResponse;

import java.util.Optional;

public interface RoomTypeService {

    CreateRoomTypeResponse createRoomType(CreateRoomTypeRequest createRoomTypeRequest);

    Optional<RoomType> getRoomTypeByName(String roomType);
}
