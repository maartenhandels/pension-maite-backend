package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
import com.pensionmaite.pensionmaitebackend.exception.DBException;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.repository.ReservationRepo;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import com.pensionmaite.pensionmaitebackend.service.RoomService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Log4j2
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepo reservationRepo;

    @Autowired
    RoomService roomService;


    @Override
    public Reservation createReservation(CreateReservationRequest createReservationRequest)
            throws InvalidRequestException, DBException {

        log.debug(createReservationRequest);

        // Validate Availability
        if(!roomService.areRoomsAvailable(
                List.copyOf(createReservationRequest.getRooms()),
                createReservationRequest.getCheckinDate(),
                createReservationRequest.getCheckoutDate())) {
            throw new InvalidRequestException("Rooms are not available for selected dates");
        }

        // Create the Reservation Object
        Reservation reservation = new Reservation(
                createReservationRequest.getCheckinDate(),
                createReservationRequest.getCheckoutDate(),
                createReservationRequest.getContactData(),
                Timestamp.from(Instant.now()),
                createReservationRequest.getRooms()
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
