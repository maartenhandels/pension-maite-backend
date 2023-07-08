package com.pensionmaite.pensionmaitebackend.events.response;

import com.pensionmaite.pensionmaitebackend.events.dto.AvailableRoomType;
import lombok.Data;

import java.util.List;

@Data
public class AvailableRoom {

    List<AvailableRoomType> availableRoomTypes;

    private Integer numberOfNights;
}
