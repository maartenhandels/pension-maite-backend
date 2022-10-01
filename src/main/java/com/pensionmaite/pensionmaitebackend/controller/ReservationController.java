package com.pensionmaite.pensionmaitebackend.controller;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.events.ReservationRequest;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping("/create")
    private Reservation createReservation(ReservationRequest reservationRequest) {
        return reservationService.createReservation(reservationRequest);
    }
}
