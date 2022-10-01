package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.events.ReservationRequest;

public interface ReservationService {

    public Reservation createReservation(ReservationRequest reservationRequest);
}
