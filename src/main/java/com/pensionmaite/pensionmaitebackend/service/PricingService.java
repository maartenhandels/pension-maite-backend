package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import com.pensionmaite.pensionmaitebackend.events.request.CreatePricingRequest;

public interface PricingService {

    Pricing createPricing(CreatePricingRequest createPricingRequest);
}
