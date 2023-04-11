package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.events.request.CreatePricingRequest;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.repository.PricingRepo;
import com.pensionmaite.pensionmaitebackend.service.PricingService;
import com.pensionmaite.pensionmaitebackend.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PricingServiceImpl implements PricingService {

    @Autowired
    PricingRepo pricingRepo;

    @Autowired
    RoomTypeService roomTypeService;


    @Override
    public Pricing createPricing(CreatePricingRequest createPricingRequest) throws InvalidRequestException {
        return pricingRepo.save(createPricingEntity(createPricingRequest));
    }

    private Pricing createPricingEntity(CreatePricingRequest createPricingRequest) throws InvalidRequestException {

        // Look for the room type in the db and throw error if not found
        Optional<RoomType> roomType = roomTypeService.getRoomTypeByName(createPricingRequest.getRoomType());
        if (roomType.isEmpty()) {
            throw new InvalidRequestException("Room Type does not exist");
        }

        // Create a new pricing object if room type is found
        Pricing pricing = new Pricing();
        pricing.setRoomType(roomType.get());
        pricing.setStartDate(createPricingRequest.getStartDate());
        pricing.setEndDate(createPricingRequest.getEndDate());
        pricing.setPrice(createPricingRequest.getPrice());

        return pricing;
    }
}
