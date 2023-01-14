package com.pensionmaite.pensionmaitebackend.controller;

import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import com.pensionmaite.pensionmaitebackend.events.response.CreateReservationResponse;
import com.pensionmaite.pensionmaitebackend.exception.DBException;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping("/create")
    public ApiResponse<CreateReservationResponse> createReservation(@RequestBody CreateReservationRequest reservationRequest) {

        ApiResponse<CreateReservationResponse> response = new ApiResponse<>();

        // Check the request is valid
        List<String> errors = CreateReservationRequest.validate(reservationRequest);
        if (CollectionUtils.isNotEmpty(errors)) {
            response.setError(errors.get(0));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // Process the request
        try {
            response.setData(new CreateReservationResponse(reservationService.createReservation(reservationRequest)));
        } catch (InvalidRequestException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
            log.error(e.getMessage());
            return response;
        } catch (DBException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            log.error(e.getMessage());
            return response;
        }

        response.setStatus(HttpStatus.OK);
        return response;
    }
}
