package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
import com.pensionmaite.pensionmaitebackend.events.response.ReservationConfirmation;

import java.util.Optional;

public interface ReservationService {

    ReservationConfirmation createReservation(CreateReservationRequest reservationRequest);

    Optional<ReservationConfirmation> getReservationDetails(String email, String reservationCode);
}
