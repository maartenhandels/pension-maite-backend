package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
import com.pensionmaite.pensionmaitebackend.exception.InvalidContactDataException;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.model.ContactData;
import com.pensionmaite.pensionmaitebackend.repository.ReservationRepo;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

import static com.pensionmaite.pensionmaitebackend.util.EmailValidator.IsValidEmail;

@Service
@Log4j2
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepo reservationRepo;


    @Override
    public Reservation createReservation(CreateReservationRequest reservationRequest) throws InvalidRequestException {

        log.debug(reservationRequest);

        // Validate Data and availability
        validateRequest(reservationRequest);
        validateRoomsAvailability(reservationRequest.getRooms());

        // Create the Reservation Object
        Reservation reservation = new Reservation(
                reservationRequest.getCheckinDate(),
                reservationRequest.getCheckoutDate(),
                reservationRequest.getContactData(),
                Timestamp.from(Instant.now()),
                reservationRequest.getRooms()
        );

        // Save into the db
        return reservationRepo.save(reservation);
    }

    private void validateRequest(CreateReservationRequest reservationRequest) throws InvalidRequestException {
        if (reservationRequest == null) {
            throw new InvalidRequestException("Reservation info is missing");
        }


        if (reservationRequest.getRooms() == null) {
            throw new InvalidRequestException("Rooms data is missing");
        }

        try {
            validateContactData(reservationRequest.getContactData());
        } catch (InvalidContactDataException exception) {
            throw new InvalidRequestException(exception.getMessage());
        }
    }

    private void validateContactData(ContactData contactData) throws InvalidContactDataException {

        if (contactData == null) {
            throw new InvalidContactDataException("Contact Data is missing");
        }

        if (contactData.getEmail() == null || !IsValidEmail(contactData.getEmail())) {
            throw new InvalidContactDataException("Email is missing or invalid");
        }
    }

    private void validateRoomsAvailability(Set<Room> rooms) {

        for (Room room : rooms) {

        }
    }
}
