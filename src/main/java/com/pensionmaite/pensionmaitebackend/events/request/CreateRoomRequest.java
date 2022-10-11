package com.pensionmaite.pensionmaitebackend.events.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequest {

    @JsonProperty(required = true)
    private Integer roomNumber;

    @JsonProperty(required = true)
    private String roomType;

    private String description;
}
