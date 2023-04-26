package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import com.pensionmaite.pensionmaitebackend.events.request.CreatePricingRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PricingService {

    Pricing createPricing(CreatePricingRequest createPricingRequest);

    Pricing createDefaultPricing(String roomType, BigDecimal price);

    List<Pricing> getRoomTypePrice(String roomType);

    BigDecimal getTotalStayPrice(Map<String, Integer> roomTypes, LocalDate checkinDate, LocalDate checkoutDate);
}
