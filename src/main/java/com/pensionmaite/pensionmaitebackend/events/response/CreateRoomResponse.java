package com.pensionmaite.pensionmaitebackend.events.response;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomResponse {

    private Integer roomNumber;

    private String roomType;

    private String description;
}
