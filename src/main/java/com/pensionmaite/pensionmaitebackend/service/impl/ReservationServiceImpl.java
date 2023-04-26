package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
import com.pensionmaite.pensionmaitebackend.exception.DBException;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.repository.ReservationRepo;
import com.pensionmaite.pensionmaitebackend.service.PricingService;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import com.pensionmaite.pensionmaitebackend.service.RoomService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
@Log4j2
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepo reservationRepo;

    @Autowired
    RoomService roomService;

    @Autowired
    PricingService pricingService;


    @Override
    public Reservation createReservation(CreateReservationRequest createReservationRequest)
            throws InvalidRequestException, DBException {

        log.debug(createReservationRequest);

        Map<String, Integer> requestedRoomTypes = createReservationRequest.getRoomTypes();
        log.debug("Room Types: {}", requestedRoomTypes);

        // Get Available Rooms By Type
        Map<String, List<Room>> availableRoomsByType = roomService.getAvailableRoomsByTypes(
                createReservationRequest.getCheckinDate(),
                createReservationRequest.getCheckoutDate(),
                new ArrayList<>(requestedRoomTypes.keySet()));

        log.debug("Available Rooms: {}", availableRoomsByType);

        Set<Room> roomsToBeReserved = new HashSet<>();

        for (String roomType:requestedRoomTypes.keySet()) {
            List<Room> availableRooms = availableRoomsByType.getOrDefault(roomType, new ArrayList<>());
            if (availableRooms.size() >= requestedRoomTypes.get(roomType)) {
                roomsToBeReserved.addAll(availableRooms.subList(0, requestedRoomTypes.get(roomType)));
            } else {
                throw new InvalidRequestException("Requested Rooms are not available for the selected dates");
            }
        }

        BigDecimal reservationTotalPrice = pricingService.getTotalStayPrice(requestedRoomTypes,
                createReservationRequest.getCheckinDate(), createReservationRequest.getCheckoutDate());

        // Create the Reservation Object
        Reservation reservation = new Reservation(
                createReservationRequest.getCheckinDate(),
                createReservationRequest.getCheckoutDate(),
                createReservationRequest.getContactData(),
                Timestamp.from(Instant.now()),
                reservationTotalPrice,
                roomsToBeReserved
        );

        // Save into the db
        try {
            return reservationRepo.save(reservation);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DBException("Something went wrong while processing the request");
        }
    }
}
