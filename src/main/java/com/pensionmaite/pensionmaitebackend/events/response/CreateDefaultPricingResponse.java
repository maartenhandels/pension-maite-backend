package com.pensionmaite.pensionmaitebackend.events.response;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CreateDefaultPricingResponse {

    private Long id;

    private String roomType;

    private BigDecimal price;

    public CreateDefaultPricingResponse(Pricing pricing) {
        this(pricing.getId(),
                pricing.getRoomType().getName(),
                pricing.getPrice());
    }
}
