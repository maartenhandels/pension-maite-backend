package com.pensionmaite.pensionmaitebackend.events;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequest {

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

}
