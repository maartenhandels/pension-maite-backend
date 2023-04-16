package com.pensionmaite.pensionmaitebackend.controller;

import com.pensionmaite.pensionmaitebackend.events.request.CreateDefaultPricingRequest;
import com.pensionmaite.pensionmaitebackend.events.request.CreatePricingRequest;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomRequest;
import com.pensionmaite.pensionmaitebackend.events.request.CreateRoomTypeRequest;
import com.pensionmaite.pensionmaitebackend.events.response.*;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.exception.UniqueConstraintException;
import com.pensionmaite.pensionmaitebackend.service.PricingService;
import com.pensionmaite.pensionmaitebackend.service.RoomService;
import com.pensionmaite.pensionmaitebackend.service.RoomTypeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    RoomService roomService;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    PricingService pricingService;

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



    @PostMapping("/room/type/create")
    public ApiResponse<CreateRoomTypeResponse> createRoomType(@RequestBody CreateRoomTypeRequest request) {

        ApiResponse<CreateRoomTypeResponse> response = new ApiResponse();

        try {
            response.setData(roomTypeService.createRoomType(request));
            response.setStatus(HttpStatus.OK);
        } catch (UniqueConstraintException e) {
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @PostMapping("/room/pricing/add")
    public ApiResponse<CreatePricingResponse> createRoomType(@RequestBody CreatePricingRequest request) {

        ApiResponse<CreatePricingResponse> response = new ApiResponse();
        Optional<List<String>> errors = CreatePricingRequest.validate(request);
        if (errors.isPresent()) {
            response.setError(errors.get().get(0));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        try {
            response.setData(new CreatePricingResponse(pricingService.createPricing(request)));
            response.setStatus(HttpStatus.OK);
        } catch (UniqueConstraintException | InvalidRequestException e) {
            log.error(e.getMessage(), e);
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    @PostMapping("/room/pricing/add-default")
    public ApiResponse<CreateDefaultPricingResponse> createRoomType(@RequestBody CreateDefaultPricingRequest request) {

        ApiResponse<CreateDefaultPricingResponse> response = new ApiResponse();
        Optional<List<String>> errors = CreateDefaultPricingRequest.validate(request);
        if (errors.isPresent()) {
            response.setError(errors.get().get(0));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

        try {
            response.setData(new CreateDefaultPricingResponse(pricingService.createDefaultPricing(
                    request.getRoomType(),
                    request.getPrice())));
            response.setStatus(HttpStatus.OK);
        } catch (UniqueConstraintException | InvalidRequestException e) {
            log.error(e.getMessage(), e);
            response.setError(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
