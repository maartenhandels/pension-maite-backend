package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;

public interface ReservationService {

    public Reservation createReservation(CreateReservationRequest reservationRequest);
}
