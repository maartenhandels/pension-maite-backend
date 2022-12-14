package com.pensionmaite.pensionmaitebackend.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pensionmaite.pensionmaitebackend.model.ContactData;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq_generator")
    @SequenceGenerator(name = "reservation_seq_generator", sequenceName = "seq_reservation", allocationSize = 1)
    private Long reservationId;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private Timestamp reservationDate;

    @Transient
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservation_room",
            joinColumns = @JoinColumn(name = "reservationId"),
            inverseJoinColumns = @JoinColumn(name = "roomNumber"))
    private Set<Room> reservationRooms;

    public Reservation(LocalDate checkinDate, LocalDate checkoutDate, ContactData contactData, Timestamp reservationDate, Set<Room> reservationRooms) {
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        if (contactData != null) {
            this.email = contactData.getEmail();
            this.firstName = contactData.getFirstName();
            this.lastName = contactData.getLastName();
            this.phone = contactData.getPhone();
        }
        this.reservationDate = reservationDate;
        this.reservationRooms = reservationRooms;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", reservationDate=" + reservationDate +
                ", price=" + price +
                '}';
    }
}
