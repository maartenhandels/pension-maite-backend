package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomRequest;
import com.pensionmaite.pensionmaitebackend.events.response.AvailableRoom;
import com.pensionmaite.pensionmaitebackend.events.response.NewRoom;
import com.pensionmaite.pensionmaitebackend.events.response.RoomInfo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RoomService {

    List<RoomInfo> getAllRooms();

    NewRoom createRoom(CreateRoomRequest createRoomRequest);

    AvailableRoom processAvailableRoomsRequest(LocalDate checkinDate, LocalDate checkoutDate);

    List<com.pensionmaite.pensionmaitebackend.entity.Room> getAvailableRooms(LocalDate checkinDate, LocalDate checkoutDate);

    Map<String, List<com.pensionmaite.pensionmaitebackend.entity.Room>> getAvailableRoomsByTypes(LocalDate checkinDate, LocalDate checkoutDate, List<String> roomTypes);

    boolean areRoomsAvailable(List<com.pensionmaite.pensionmaitebackend.entity.Room> rooms, LocalDate checkinDate, LocalDate checkoutDate);
}

