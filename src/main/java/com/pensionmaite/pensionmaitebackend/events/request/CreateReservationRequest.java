package com.pensionmaite.pensionmaitebackend.events.request;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.model.ContactData;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateReservationRequest {

    private Set<Room> rooms;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private ContactData contactData;

}
