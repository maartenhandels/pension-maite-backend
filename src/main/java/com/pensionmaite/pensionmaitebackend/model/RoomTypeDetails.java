package com.pensionmaite.pensionmaitebackend.model;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import lombok.Data;

/**
 * Class used to map Rooms by RoomType, includes the RoomType object and the amount of rooms of that same type
 */
@Data
public class RoomTypeDetails {

    private RoomType roomType;

    private int count;
}
