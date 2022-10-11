package com.pensionmaite.pensionmaitebackend.events.response;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateRoomTypeResponse {

    public Integer id;

    public String name;

    public Integer capacity;

    public CreateRoomTypeResponse(RoomType roomType) {
        this.id = roomType.getId();
        this.name = roomType.getName();
        this.capacity = roomType.getCapacity();
    }

}
