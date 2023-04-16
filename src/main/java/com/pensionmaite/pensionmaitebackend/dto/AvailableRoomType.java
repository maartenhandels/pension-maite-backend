package com.pensionmaite.pensionmaitebackend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AvailableRoomType {

    private String roomType;

    private Integer availableRooms;

    private BigDecimal totalPrice;

    public AvailableRoomType(String roomType, Integer availableRooms) {
        this.roomType = roomType;
        this.availableRooms = availableRooms;
    }
}
