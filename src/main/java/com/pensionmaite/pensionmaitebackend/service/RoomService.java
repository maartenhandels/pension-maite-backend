package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.events.request.AvailableRoomsRequest;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomRequest;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomResponse;

import java.util.List;

public interface RoomService {

    CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest);

    List<Room> getAvailableRooms(AvailableRoomsRequest availableRoomsRequest);
}

