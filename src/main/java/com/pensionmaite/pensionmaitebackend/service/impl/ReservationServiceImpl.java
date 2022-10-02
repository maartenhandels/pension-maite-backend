package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Customer;
import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.events.ReservationRequest;
import com.pensionmaite.pensionmaitebackend.repository.ReservationRepo;
import com.pensionmaite.pensionmaitebackend.service.CustomerService;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@Log4j2
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    ReservationRepo reservationRepo;

    @Autowired
    CustomerService customerService;

    @Override
    public Reservation createReservation(ReservationRequest reservationRequest) {

        log.debug(reservationRequest);

        Customer customer;

        if (reservationRequest.getCustomer() != null && reservationRequest.getCustomer().getId() != null) {
            customer = customerService.getCustomer(reservationRequest.getCustomer().getId());
        } else {
            customer = customerService.saveCustomer(reservationRequest.getCustomer());
        }

        Reservation reservation = new Reservation(reservationRequest.getCheckinDate(), reservationRequest.getCheckoutDate());
        reservation.setReservationDate(Timestamp.from(Instant.now()));
        reservation.setCustomer(customer);

        return reservationRepo.save(reservation);
    }
}
