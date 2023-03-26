package com.pensionmaite.pensionmaitebackend.controller;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.events.request.AvailableRoomsRequest;
import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/get-available")
    private ApiResponse<List<Room>> getAvailableRooms(@RequestBody AvailableRoomsRequest availableRoomsRequest) {

        ApiResponse<List<Room>> response = new ApiResponse<>();

        // Validate request
        Optional<List<String>> errors = AvailableRoomsRequest.validate(availableRoomsRequest);

        // Return bad request if errors found
        if (errors.isPresent()) {
            response.setError(errors.get().get(0));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // Process request if no request is valid
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
