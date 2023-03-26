package com.pensionmaite.pensionmaitebackend.controller;

import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomRequest;
import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomResponse;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    RoomService roomService;

    @PostMapping("/room/create")
    private ApiResponse<CreateRoomResponse> createRoom(@RequestBody CreateRoomRequest createRoomRequest) {

        ApiResponse<CreateRoomResponse> response = new ApiResponse<>();

        try {
            response.setData(roomService.createRoom(createRoomRequest));
            response.setStatus(HttpStatus.OK);
        } catch (InvalidRequestException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
