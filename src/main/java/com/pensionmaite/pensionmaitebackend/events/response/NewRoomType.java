package com.pensionmaite.pensionmaitebackend.events.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class NewRoomType {

    public Integer id;

    public String name;

    public Integer capacity;

    public String imageFilename;

    public NewRoomType(com.pensionmaite.pensionmaitebackend.entity.RoomType roomType) {
        this.id = roomType.getId();
        this.name = roomType.getName();
        this.capacity = roomType.getCapacity();
        this.imageFilename = roomType.getImageFilename();
    }

}
