package com.pensionmaite.pensionmaitebackend.events.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewRoom {

    private Integer roomNumber;

    private String roomType;

    private String description;
}
