package com.pensionmaite.pensionmaitebackend.events.response;

import com.pensionmaite.pensionmaitebackend.dto.AvailableRoomType;
import lombok.Data;

import java.util.List;

@Data
public class AvailableRoomResponse {

    List<AvailableRoomType> availableRoomTypes;

    private Integer numberOfNights;
}
