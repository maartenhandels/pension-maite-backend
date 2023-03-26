package com.pensionmaite.pensionmaitebackend.events.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class AvailableRoomsRequest {

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    public static Optional<List<String>> validate(AvailableRoomsRequest availableRoomsRequest) {

        if (availableRoomsRequest == null) {
            return Optional.of(Collections.singletonList("Request object can not be empty"));
        }

        if (availableRoomsRequest.getCheckinDate() == null) {
            return Optional.of(Collections.singletonList("Checkin date can not be empty"));
        }

        if (availableRoomsRequest.getCheckoutDate() == null) {
            return Optional.of(Collections.singletonList("Checkout date can not be empty"));
        }

        return Optional.empty();
    }
}
