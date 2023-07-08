package com.pensionmaite.pensionmaitebackend.events.response;

import com.pensionmaite.pensionmaitebackend.events.dto.ReservedRoomType;
import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.model.ContactData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationConfirmation {

    private String reservationId;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private ContactData contactData;

    private BigDecimal totalPrice;

    List<ReservedRoomType> reservedRoomTypes;

    public ReservationConfirmation(Reservation reservation) {
        this.reservationId = reservation.getReservationId();
        this.checkinDate = reservation.getCheckinDate();
        this.checkoutDate = reservation.getCheckoutDate();
        this.contactData = new ContactData(
                reservation.getFirstName(),
                reservation.getLastName(),
                reservation.getEmail(),
                reservation.getPhone()
        );
        this.totalPrice = reservation.getTotalPrice();
        this.reservedRoomTypes = ReservedRoomType.parseRoomListToReservedRoomTypeList(reservation.getReservationRooms());
    }

}
