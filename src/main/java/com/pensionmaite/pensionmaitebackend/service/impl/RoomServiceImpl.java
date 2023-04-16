package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.dto.AvailableRoomType;
import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomRequest;
import com.pensionmaite.pensionmaitebackend.events.response.AvailableRoomResponse;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomResponse;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.exception.ValueNotFoundException;
import com.pensionmaite.pensionmaitebackend.repository.ReservationRepo;
import com.pensionmaite.pensionmaitebackend.repository.RoomRepo;
import com.pensionmaite.pensionmaitebackend.repository.RoomTypeRepo;
import com.pensionmaite.pensionmaitebackend.service.PricingService;
import com.pensionmaite.pensionmaitebackend.service.RoomService;
import com.pensionmaite.pensionmaitebackend.util.DatesUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Autowired
    PricingService pricingService;


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
        Optional<RoomType> roomType = roomTypeRepo.findByName(createRoomRequest.getRoomType().toUpperCase().trim());

        // Throw an exception if the provided room type is invalid
        if (roomType.isEmpty()) {
            throw new InvalidRequestException("Invalid room type provided");
        }

        // Throw an exception if the provided room number is not unique
        if (roomRepo.findById(createRoomRequest.getRoomNumber()).isPresent()) {
            throw new InvalidRequestException("Room Number not unique");
        }

        // Create a new Room object based on the provided details
        Room room = new Room(
                createRoomRequest.getRoomNumber(),
                roomType.get(),
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
     * Process the available rooms request based on the provided check-in and check-out dates.
     *
     * @param checkinDate the check-in date
     * @param checkoutDate the check-out date
     * @return the available room response with a list of available room types and their corresponding prices for the
     * given dates
     */
    @Override
    public AvailableRoomResponse processAvailableRoomsRequest(LocalDate checkinDate, LocalDate checkoutDate) {

        AvailableRoomResponse availableRoomResponse = new AvailableRoomResponse();

        List<Room> availableRooms = getAvailableRooms(checkinDate, checkoutDate);
        availableRoomResponse.setAvailableRoomTypes(parseToAvailableRoomTypes(availableRooms));

        availableRoomResponse.setAvailableRoomTypes(
                setAvailableRoomTypesPricing(
                        availableRoomResponse.getAvailableRoomTypes(),
                        checkinDate,
                        checkoutDate));

        availableRoomResponse.setNumberOfNights(DatesUtil.getNumberOfNights(checkinDate, checkoutDate));

        return availableRoomResponse;
    }

    /**
     * Returns a list of {@code Room} objects that are available for booking during the specified check-in and check-out dates.
     *
     * @param checkinDate the check-in date for the room availability period
     * @param checkoutDate the check-out date for the room availability period
     * @return a list of available {@code Room} objects
     */
    @Override
    public List<Room> getAvailableRooms(LocalDate checkinDate, LocalDate checkoutDate) {

        log.debug("Finding available rooms for dates {} to {}", checkinDate, checkoutDate);

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

    private List<AvailableRoomType> parseToAvailableRoomTypes(List<Room> rooms) {

        // Key: roomType, Value: number of rooms available
        Map<String, Integer> availableRoomTypes = countRoomsByType(rooms);

        // Return a list of AvailableRoomType objects created with the Hashmap data
        return availableRoomTypes.keySet()
                .stream()
                .map(type -> new AvailableRoomType(type, availableRoomTypes.get(type)))
                .collect(Collectors.toList());
    }

    /**
     * Returns a HashMap where the key is the room type and the value is the number of rooms of that type
     *
     * @param rooms a List of Room objects to count by type
     * @return a HashMap with keys representing each room type and values representing the number of rooms of that type
     */
    private Map<String, Integer> countRoomsByType(List<Room> rooms) {
        // create a new HashMap to store the room type counts
        Map<String, Integer> roomTypeCountMap = new HashMap<>();
        // loop through each room in the list
        for (Room room : rooms) {
            // check if room type (and room type name) are not null
            if (room.getRoomType() != null && StringUtils.isNotBlank(room.getRoomType().getName())) {
                // get the current count for the room type from the roomTypeCountMap
                Integer count = roomTypeCountMap.get(room.getRoomType().getName());
                // initialize it to 0 if it doesn't exist yet
                if (count == null) {
                    count = 0;
                }
                // add the current room to the count for its room type in the roomTypeCountMap
                roomTypeCountMap.put(room.getRoomType().getName(), count + 1);
            } else {
                log.error("Room {} has room type (or room type name) null", room);
            }
        }
        // return the completed roomTypeCountMap
        return roomTypeCountMap;
    }

    private List<AvailableRoomType> setAvailableRoomTypesPricing(List<AvailableRoomType> availableRoomTypes,
                                                                 LocalDate checkinDate,
                                                                 LocalDate checkoutDate) {
        // loop over the roomTypes, and set the total price
        for(AvailableRoomType availableRoomType:availableRoomTypes) {
                availableRoomType.setTotalPrice(pricingService.getTotalStayPrice(
                        availableRoomType.getRoomType(),
                        checkinDate,
                        checkoutDate));
        }

        return availableRoomTypes
                .stream()
                .filter(r -> r.getTotalPrice() != null).collect(Collectors.toList());
    }

}
