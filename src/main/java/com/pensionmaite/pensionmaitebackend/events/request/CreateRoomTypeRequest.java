package com.pensionmaite.pensionmaitebackend.events.request;

import lombok.Getter;

@Getter
public class CreateRoomTypeRequest {

    public String name;

    public Integer capacity;
}
