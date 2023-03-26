package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.events.request.AvailableRoomsRequest;
import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
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

import java.time.LocalDate;
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


    /**
     * Creates a new {@code Room} object based on the provided {@code CreateRoomRequest} and saves it to the repository.
     *
     * @param createRoomRequest the {@code CreateRoomRequest} object containing the details of the room to be created
     * @return a {@code CreateRoomResponse} object containing the details of the newly created room
     * @throws InvalidRequestException if the provided room type is invalid or if the provided room number is not unique
     */
    @Override
    public CreateRoomResponse createRoom(CreateRoomRequest createRoomRequest) {

        // Retrieve the RoomType object associated with the provided room type name
        RoomType roomType = roomTypeRepo.findByName(createRoomRequest.getRoomType().toUpperCase().trim());

        // Throw an exception if the provided room type is invalid
        if (roomType == null) {
            throw new InvalidRequestException("Invalid room type provided");
        }

        // Throw an exception if the provided room number is not unique
        if (roomRepo.findById(createRoomRequest.getRoomNumber()).isPresent()) {
            throw new InvalidRequestException("Room Number not unique");
        }

        // Create a new Room object based on the provided details
        Room room = new Room(
                createRoomRequest.getRoomNumber(),
                roomType,
                createRoomRequest.getDescription());

        // Save the new Room object to the repository
        room = roomRepo.save(room);

        // Create and return a new CreateRoomResponse object containing the details of the newly created room
        return new CreateRoomResponse(
                room.getRoomNumber(),
                room.getRoomType().getName(),
                room.getDescription());
    }

    /**
     * Returns a list of available rooms based on the provided {@code AvailableRoomsRequest}.
     *
     * @param availableRoomsRequest the request containing the check-in and check-out dates
     * @return a list of available {@code Room} objects
     */
    @Override
    public List<Room> getAvailableRooms(AvailableRoomsRequest availableRoomsRequest) {

        log.debug("AvailableRoomsRequest {}", availableRoomsRequest.toString());

        return getAvailableRooms(availableRoomsRequest.getCheckinDate(), availableRoomsRequest.getCheckoutDate());
    }

    /**
     * Checks if all the rooms in the provided list are available during the specified check-in and check-out dates.
     *
     * @param rooms the list of rooms to check for availability
     * @param checkinDate the check-in date for the room availability period
     * @param checkoutDate the check-out date for the room availability period
     * @return {@code true} if all the rooms are available during the specified period, {@code false} otherwise
     */
    @Override
    public boolean areRoomsAvailable(List<Room> rooms, LocalDate checkinDate, LocalDate checkoutDate) {

        for (Room room : rooms) {
            if(!isRoomAvailable(room, checkinDate, checkoutDate)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a list of {@code Room} objects that are available for booking during the specified check-in and check-out dates.
     *
     * @param checkinDate the check-in date for the room availability period
     * @param checkoutDate the check-out date for the room availability period
     * @return a list of available {@code Room} objects
     */
    private List<Room> getAvailableRooms(LocalDate checkinDate, LocalDate checkoutDate) {

        log.info("Finding available rooms...");

        // Retrieve a list of reservations between the provided check-in and check-out dates
        List<Reservation> reservations = reservationRepo.findReservationsBetweenDates(
                checkinDate,
                checkoutDate);

        log.debug("Existing reservations: " + reservations);

        // Create a set of room numbers for the reserved rooms
        Set<Integer> reservedRoomNumbers = new HashSet<>();

        // Add the room numbers for all reserved rooms to the set
        for (Reservation reservation:reservations) {
            reservedRoomNumbers.addAll(reservation.getReservationRooms()
                    .stream()
                    .map(Room::getRoomNumber)
                    .collect(Collectors.toSet()));
        }

        log.debug("Associated Room Numbers: " + reservedRoomNumbers);

        // Retrieve all rooms from the room repository and filter out the reserved rooms
        List<Room> rooms = IterableUtils.toList(roomRepo.findAll());
        log.debug("Rooms: " + rooms);
        rooms = rooms.stream()
                .filter(r -> !reservedRoomNumbers.contains(r.getRoomNumber()))
                .collect(Collectors.toList());

        log.debug("Available Rooms: " + rooms);

        // Return the list of available rooms
        return rooms;
    }

    /**
     * Determines whether the specified {@code Room} object is available for booking during the specified check-in and check-out dates.
     *
     * @param room the {@code Room} object to check availability for
     * @param checkinDate the check-in date for the room availability period
     * @param checkoutDate the check-out date for the room availability period
     * @return {@code true} if the specified {@code Room} object is available for booking, {@code false} otherwise
     */
    private boolean isRoomAvailable(Room room, LocalDate checkinDate, LocalDate checkoutDate) {

        // Retrieve a list of reservations between the provided check-in and check-out dates
        List<Reservation> reservations = reservationRepo.findReservationsBetweenDates(checkinDate, checkoutDate);

        // Iterate through each reservation and check if the specified room is reserved
        for (Reservation reservation:reservations) {
            List<Integer> roomNumbers = reservation.getReservationRooms().stream().map(Room::getRoomNumber).collect(Collectors.toList());
            log.debug("RoomNumbers: {}", roomNumbers);
            // If the specified room is reserved, return false
            if (roomNumbers.contains(room.getRoomNumber())) {
                return false;
            }
        }
        // If the specified room is not reserved, return true
        return true;
    }
}
