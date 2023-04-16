package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomRequest;
import com.pensionmaite.pensionmaitebackend.events.response.AvailableRoomResponse;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomResponse;

import java.time.LocalDate;
import java.util.List;

public interface RoomService {

    CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest);

    AvailableRoomResponse processAvailableRoomsRequest(LocalDate checkinDate, LocalDate checkoutDate);

    List<Room> getAvailableRooms(LocalDate checkinDate, LocalDate checkoutDate);

    boolean areRoomsAvailable(List<Room> rooms, LocalDate checkinDate, LocalDate checkoutDate);
}

