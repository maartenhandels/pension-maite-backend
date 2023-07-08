package com.pensionmaite.pensionmaitebackend.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class AvailableRoomType {

    private String roomType;

    private Integer capacity;

    private String imageFilename;

    private Integer availableRooms;

    private BigDecimal totalPrice;


    public AvailableRoomType(String roomType, Integer capacity, String imageFilename, Integer availableRooms) {
        this.roomType = roomType;
        this.capacity = capacity;
        this.imageFilename = imageFilename;
        this.availableRooms = availableRooms;
    }
}
