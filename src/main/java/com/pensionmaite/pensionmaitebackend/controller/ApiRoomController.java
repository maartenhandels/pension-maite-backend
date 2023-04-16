package com.pensionmaite.pensionmaitebackend.controller;

import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import com.pensionmaite.pensionmaitebackend.events.response.AvailableRoomResponse;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.service.RoomService;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/room")
public class ApiRoomController {

    @Autowired
    RoomService roomService;

    @GetMapping("/get-available")
    private ApiResponse<AvailableRoomResponse> getAvailableRooms(@RequestParam(required = true) String checkinDate,
                                                                        @RequestParam(required = true) String checkoutDate) {

        ApiResponse<AvailableRoomResponse> response = new ApiResponse<>();

        boolean badRequest = false;
        LocalDate parsedCheckinDate = null;
        LocalDate parsedCheckoutDate = null;

        // Parse to Localdates and validate dates
        try {
            parsedCheckinDate = LocalDate.parse(checkinDate);
            parsedCheckoutDate = LocalDate.parse(checkoutDate);
            if (!parsedCheckoutDate.isAfter(parsedCheckinDate)) {
                badRequest = true;
            }
        } catch (DateTimeParseException e) {
            badRequest = true;
            log.error(e.getMessage());
        }

        // Return bad request if errors found
        if (badRequest) {
            response.setError("Checkin date and/or Checkout date is empty or incorrect.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // Process request if no request is valid
        try {
            response.setData(roomService.processAvailableRoomsRequest(parsedCheckinDate, parsedCheckoutDate));
            response.setStatus(HttpStatus.OK);
        } catch (InvalidRequestException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
