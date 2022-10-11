package com.pensionmaite.pensionmaitebackend.events.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AvailableRoomsRequest {

    private LocalDate checkinDate;

    private LocalDate checkoutDate;
}
