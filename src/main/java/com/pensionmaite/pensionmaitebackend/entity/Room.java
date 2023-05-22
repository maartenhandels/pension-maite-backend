package com.pensionmaite.pensionmaitebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@Entity
@Table(name = "room")
public class Room {

    @Id
    private Integer roomNumber;

    @JsonIgnore
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
                ", roomType=" + roomType.getName() +
                ", description='" + description + '\'' +
                '}';
    }
}
