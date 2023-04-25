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
import com.pensionmaite.pensionmaitebackend.model.RoomTypeDetails;
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
        availableRoomResponse.setAvailableRoomTypes(
                parseToAvailableRoomTypes(availableRooms, checkinDate, checkoutDate));

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

        Set<Integer> reservedRoomNumbers = getReservedRoomNumbers(checkinDate, checkoutDate);
        log.debug("Reserved Room Numbers: " + reservedRoomNumbers);

        // Retrieve all rooms from the room repository and filter out the reserved rooms
        List<Room> rooms = IterableUtils.toList(roomRepo.findAll());

        return filterReservedRooms(rooms, reservedRoomNumbers);
    }

    @Override
    public Map<String, List<Room>> getAvailableRoomsByTypes(LocalDate checkinDate,
                                                            LocalDate checkoutDate,
                                                            List<String> roomTypes) {

        log.debug("Finding available rooms for dates {} to {}", checkinDate, checkoutDate);

        Set<Integer> reservedRoomNumbers = getReservedRoomNumbers(checkinDate, checkoutDate);
        log.debug("Reserved Room Numbers: " + reservedRoomNumbers);

        // Retrieve all rooms from the room repository with matching type and filter out the reserved rooms
        List<Room> rooms = IterableUtils.toList(roomRepo.findByCategoryNames(roomTypes));

        rooms = filterReservedRooms(rooms, reservedRoomNumbers);

        return mapRoomsByType(rooms);
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

    private Set<Integer> getReservedRoomNumbers(LocalDate checkinDate, LocalDate checkoutDate) {
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

        return reservedRoomNumbers;
    }

    private List<Room> filterReservedRooms(List<Room> rooms, Set<Integer> reservedRoomNumbers) {
        return rooms.stream()
                .filter(r -> !reservedRoomNumbers.contains(r.getRoomNumber()))
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of rooms by their room type name.
     *
     * @param rooms the list of rooms to map.
     * @return a map where each key is a room type name and the corresponding value
     *         is a list of rooms that have that room type.
     */
    private Map<String, List<Room>> mapRoomsByType(List<Room> rooms) {
        Map<String, List<Room>> result = new HashMap<>();

        for (Room room : rooms) {
            String roomTypeName = room.getRoomType().getName();
            List<Room> matchingRooms = result.getOrDefault(roomTypeName, new ArrayList<>());
            matchingRooms.add(room);
            result.put(roomTypeName, matchingRooms);
        }

        return result;
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

    private List<AvailableRoomType> parseToAvailableRoomTypes(List<Room> rooms,
                                                              LocalDate checkinDate,
                                                              LocalDate checkoutDate) {

        // Key: roomType, Value: number of rooms available
        Map<String, RoomTypeDetails> availableRoomsByType = getRoomsByType(rooms);

        // Return a list of AvailableRoomType objects created with the Hashmap data
        List<AvailableRoomType> availableRoomTypes = availableRoomsByType.values()
                .stream()
                .map(type -> new AvailableRoomType(
                        type.getRoomType().getName(),
                        type.getRoomType().getCapacity(),
                        type.getRoomType().getImageFilename(),
                        type.getCount()))
                .collect(Collectors.toList());

        return setAvailableRoomTypesPricing(availableRoomTypes, checkinDate, checkoutDate);
    }

    /**
     * Returns a HashMap where the key is the room type and the value is the number of rooms of that type
     *
     * @param rooms a List of Room objects to count by type
     * @return a HashMap with keys representing each room type and values representing the number of rooms of that type
     */
    private Map<String, RoomTypeDetails> getRoomsByType(List<Room> rooms) {
        // create a new HashMap to store the room type counts
        Map<String, RoomTypeDetails> roomTypeMap = new HashMap<>();
        // loop through each room in the list
        for (Room room : rooms) {
            RoomType roomType = room.getRoomType();

            // check if room type (and room type name) are not null
            if (roomType != null && StringUtils.isNotBlank(roomType.getName())) {
                RoomTypeDetails roomTypeDetails = roomTypeMap.get(roomType.getName());
                if (roomTypeDetails == null) {
                    roomTypeDetails = new RoomTypeDetails();
                    roomTypeDetails.setRoomType(roomType);
                }

                roomTypeDetails.setCount(roomTypeDetails.getCount() + 1);
                roomTypeMap.put(roomType.getName(), roomTypeDetails);
            } else {
                log.error("Room {} has room type (or room type name) null", room);
            }
        }
        // return the completed roomTypeCountMap
        return roomTypeMap;
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
