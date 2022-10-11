package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.events.request.AvailableRoomsRequest;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomRequest;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomResponse;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.repository.ReservationRepo;
import com.pensionmaite.pensionmaitebackend.repository.RoomRepo;
import com.pensionmaite.pensionmaitebackend.repository.RoomTypeRepo;
import com.pensionmaite.pensionmaitebackend.service.RoomService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepo roomRepo;

    @Autowired
    RoomTypeRepo roomTypeRepo;

    @Autowired
    ReservationRepo reservationRepo;


    @Override
    public CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest) {

        RoomType roomType = roomTypeRepo.findByName(createRoomRequest.getRoomType().toUpperCase().trim());

        if (roomType == null) {
            throw new InvalidRequestException("Invalid room type provided");
        }

        if (roomRepo.findById(createRoomRequest.getRoomNumber()).isPresent()) {
            throw new InvalidRequestException("Room Number not unique");
        }

        // Create Room object
        Room room = new Room(
                createRoomRequest.getRoomNumber(),
                roomType,
                createRoomRequest.getDescription());

        // Save it
        room = roomRepo.save(room);

        // Create Response and return it
        return new CreateRoomResponse(
                room.getRoomNumber(),
                room.getRoomType().getName(),
                room.getDescription());
    }

    @Override
    public List<Room> getAvailableRooms(AvailableRoomsRequest availableRoomsRequest) {

        log.debug(availableRoomsRequest.toString());

        List<Reservation> reservations = reservationRepo.findReservationsBetweenDates(
                availableRoomsRequest.getCheckinDate(),
                availableRoomsRequest.getCheckoutDate());

        log.debug("Existing reservations: " + reservations);

        Set<Integer> reservedRoomNumbers = new HashSet<>();

        for (Reservation reservation:reservations) {
            reservedRoomNumbers.addAll(reservation.getReservationRooms()
                    .stream()
                    .map(Room::getRoomNumber)
                    .collect(Collectors.toSet()));
        }

        log.debug("Associated Room Numbers: " + reservedRoomNumbers);

        List<Room> rooms = IterableUtils.toList(roomRepo.findAll());
        log.debug("Rooms: " + rooms);
        rooms = rooms.stream()
                .filter(r -> !reservedRoomNumbers.contains(r.getRoomNumber()))
                .collect(Collectors.toList());

        log.debug("Available Rooms: " + rooms);

        return rooms;
    }
}
