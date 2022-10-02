package com.pensionmaite.pensionmaitebackend.events;

import com.pensionmaite.pensionmaitebackend.entity.Customer;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequest {

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private Customer customer;

}
