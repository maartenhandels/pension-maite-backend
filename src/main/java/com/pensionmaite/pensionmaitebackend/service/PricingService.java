package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import com.pensionmaite.pensionmaitebackend.events.request.CreatePricingRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PricingService {

    Pricing createPricing(CreatePricingRequest createPricingRequest);

    Pricing createDefaultPricing(String roomType, BigDecimal price);

    List<Pricing> getRoomTypePrice(String roomType);

    BigDecimal getTotalStayPrice(String roomType, LocalDate checkinDate, LocalDate checkoutDate);
}
