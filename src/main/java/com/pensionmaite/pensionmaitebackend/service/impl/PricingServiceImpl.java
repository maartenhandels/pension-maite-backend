package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.events.request.CreatePricingRequest;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.repository.PricingRepo;
import com.pensionmaite.pensionmaitebackend.service.PricingService;
import com.pensionmaite.pensionmaitebackend.service.RoomTypeService;
import com.pensionmaite.pensionmaitebackend.util.DatesUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Log4j2
@Service
public class PricingServiceImpl implements PricingService {

    @Autowired
    PricingRepo pricingRepo;

    @Autowired
    RoomTypeService roomTypeService;


    @Override
    public Pricing createPricing(CreatePricingRequest createPricingRequest) throws InvalidRequestException {

        log.info("Starting to create new pricing for dates {} to {}",
                createPricingRequest.getStartDate(),
                createPricingRequest.getEndDate());

        List<Pricing> roomTypePricings = getRoomTypePrice(createPricingRequest.getRoomType());
        List<Pricing> newPricings = new ArrayList<>();

        for (Pricing pricing:roomTypePricings) {
            log.debug("Comparing to pricing: {}", pricing);
            if (DatesUtil.dateRangesOverlap(
                    createPricingRequest.getStartDate(),
                    createPricingRequest.getEndDate(),
                    pricing.getStartDate(),
                    pricing.getEndDate())) {
                // If dates overlap check if it's possible that it's due to an ongoing pricing date range
                if (Stream.of(createPricingRequest.getEndDate(), pricing.getEndDate()).anyMatch(Objects::isNull)) {
                    // If so handle ongoing date ranges
                    Pricing changedPricing = handleOngoingPricing(pricing, createPricingRequest);
                    if(changedPricing != null) {
                        newPricings.add(changedPricing);
                    }
                }
            }
        }
        // If no exceptions thrown, save both the new pricing and the modified pricings (if any)
        Pricing newPricing = createPricingEntity(createPricingRequest);
        newPricings.add(newPricing);
        pricingRepo.saveAll(newPricings);

        return newPricing;
    }

    @Override
    public Pricing createDefaultPricing(String roomType, BigDecimal price) throws InvalidRequestException{
        log.info("Starting to create a default pricing for room type {} ", roomType);
        Optional<Pricing> defaultPricing = pricingRepo.findDefaultPricingForRoomType(roomType);
        if (defaultPricing.isPresent()) {
            throw new InvalidRequestException("Default pricing for type " + roomType + " already exists");
        }

        // Look for the room type in the db and throw error if not found
        Optional<RoomType> dbRoomType = roomTypeService.getRoomTypeByName(roomType);
        if (dbRoomType.isEmpty()) {
            throw new InvalidRequestException("Room Type does not exist");
        }

        return pricingRepo.save(new Pricing(dbRoomType.get(), price));
    }

    @Override
    public List<Pricing> getRoomTypePrice(String roomType) {
        return pricingRepo.findByRoomTypeName(roomType);
    }

    @Override
    public BigDecimal getTotalStayPrice(String roomType, LocalDate checkinDate, LocalDate checkoutDate) {

        log.info("Calculating total stay for a {} room, from the {} to {}", roomType, checkinDate, checkoutDate);

        // get the price list
        List<Pricing> pricingList = pricingRepo.findOverlappingPricingsForRoomType(roomType, checkinDate, checkoutDate);
        log.debug("Pricing List size: {}", pricingList.size());

        // initialize the totalPrice variable
        BigDecimal totalPrice = BigDecimal.ZERO;

        // if the price list is empty try getting the default, if not available log an error and return null
        if (pricingList.size() == 0) {
            Optional<Pricing> defaultPricing = pricingRepo.findDefaultPricingForRoomType(roomType);
            if (defaultPricing.isEmpty()) {
                log.error("Pricing not found for room Type {}", roomType);
                totalPrice = null;
            }else {
                totalPrice = BigDecimal.valueOf(DatesUtil.getNumberOfNights(checkinDate, checkoutDate))
                        .multiply(defaultPricing.get().getPrice());
            }
        } else {
            // Calculate the total price summing the price per night
            if (pricingList.size() > 1) {
                for (LocalDate date = checkinDate; date.isBefore(checkoutDate); date = date.plusDays(1)) {
                    for (Pricing pricing : pricingList) {
                        if (pricing.getStartDate().isBefore(date) &&
                                (pricing.getEndDate() == null || pricing.getEndDate().isAfter(date))) {
                            totalPrice = totalPrice.add(pricing.getPrice());
                            break;
                        }
                    }
                }
            } else {
                totalPrice = BigDecimal.valueOf(DatesUtil.getNumberOfNights(checkinDate, checkoutDate))
                        .multiply(pricingList.get(0).getPrice());
            }
        }

        return totalPrice;
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

    /**
     * Handles where one of the pricings is ongoing (i.e., the end date is null)
     * If the existing pricing is ongoing and the new pricing starts after the existing's start date, the existing one's
     * end date gets updated to not be ongoing anymore.
     *
     * @param pricing
     * @param createPricingRequest
     * @return
     * @throws InvalidRequestException
     */
    private Pricing handleOngoingPricing(Pricing pricing, CreatePricingRequest createPricingRequest)
            throws InvalidRequestException{

        // Check if the new pricing range is ongoing, if it is check the new start date is after the existing one
        if(createPricingRequest.getEndDate() == null
                && createPricingRequest.getStartDate().isBefore(pricing.getStartDate())) {
            throw new InvalidRequestException("Date Ranges overlap with an existing Pricing");
        }

        // Check if the existing pricing range is ongoing (i.e., the end date is null)
        if(pricing.getEndDate() == null) {
            if (pricing.getStartDate().isBefore(createPricingRequest.getStartDate())) {
                log.debug("Inside if statement");
                pricing.setEndDate(createPricingRequest.getStartDate().minusDays(1));
                return pricing;
            } else if(createPricingRequest.getEndDate().isAfter(pricing.getStartDate())) {
                throw new InvalidRequestException("Date Ranges overlap with an existing Pricing");
            }
        }

        return null;
    }
}
