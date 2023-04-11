package com.pensionmaite.pensionmaitebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    private Integer roomNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="room_type_id", nullable=false)
    private RoomType roomType;

    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "reservationRooms")
    Set<Reservation> reservations;

    public Room(Integer roomNumber, RoomType roomType, String description) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber=" + roomNumber +
                ", roomType=" + roomType +
                ", description='" + description + '\'' +
                '}';
    }
}
