package com.pensionmaite.pensionmaitebackend.model;

import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import lombok.Data;

@Data
public class RoomTypeDetails {

    private RoomType roomType;

    private int count;
}
