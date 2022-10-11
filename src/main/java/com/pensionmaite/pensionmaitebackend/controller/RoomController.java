package com.pensionmaite.pensionmaitebackend.controller;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.events.request.AvailableRoomsRequest;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomRequest;
import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import com.pensionmaite.pensionmaitebackend.events.response.CreateRoomResponse;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    @PostMapping("/create")
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

    @GetMapping
    private ApiResponse<List<Room>> getAvailableRooms(@RequestBody AvailableRoomsRequest availableRoomsRequest) {

        ApiResponse<List<Room>> response = new ApiResponse<>();

        try {
            response.setData(roomService.getAvailableRooms(availableRoomsRequest));
            response.setStatus(HttpStatus.OK);
        } catch (InvalidRequestException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
