package com.pensionmaite.pensionmaitebackend.controller;

import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import com.pensionmaite.pensionmaitebackend.events.response.ReservationConfirmation;
import com.pensionmaite.pensionmaitebackend.exception.DBException;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import com.pensionmaite.pensionmaitebackend.util.EmailValidator;
import com.pensionmaite.pensionmaitebackend.util.ReservationCodeGenerator;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/v1/reservation")
public class ApiReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping("/create")
    public ApiResponse<ReservationConfirmation> createReservation(@RequestBody CreateReservationRequest reservationRequest) {

        ApiResponse<ReservationConfirmation> response = new ApiResponse<>();

        // Check the request is valid
        List<String> errors = CreateReservationRequest.validate(reservationRequest);
        if (CollectionUtils.isNotEmpty(errors)) {
            response.setError(errors.get(0));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // Process the request
        try {
            response.setData(reservationService.createReservation(reservationRequest));
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

    @GetMapping("/get-details")
    public ApiResponse<ReservationConfirmation> getReservationDetails(@RequestParam(required = true) String email,
                                                                      @RequestParam(required = true) String reservationCode) {

        ApiResponse<ReservationConfirmation> response = new ApiResponse<>();

        // Check the request is valid
        if (!EmailValidator.IsValidEmail(email) || reservationCode.length() != ReservationCodeGenerator.CODE_LENGTH) {
            response.setError("Invalid Invalid or Reservation Code.");
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        // Process the request
        try {
            Optional<ReservationConfirmation> confirmation = reservationService.getReservationDetails(
                    email, reservationCode);
            if (confirmation.isPresent()) {
                response.setData(confirmation.get());
            } else {
                response.setError("No Reservation found for the provided email and reservation code.");
                response.setStatus(HttpStatus.NO_CONTENT);
                return response;
            }
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
