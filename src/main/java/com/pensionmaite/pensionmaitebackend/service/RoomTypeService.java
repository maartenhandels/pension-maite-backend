package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomTypeRequest;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomTypeResponse;

public interface RoomTypeService {

    CreateRoomTypeResponse createRoomType(CreateRoomTypeRequest createRoomTypeRequest);
}
