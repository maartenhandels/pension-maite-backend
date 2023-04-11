package com.pensionmaite.pensionmaitebackend.events.response;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreatePricingResponse {

    private Long id;

    private String roomType;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal price;

    public CreatePricingResponse(Pricing pricing) {
        this(pricing.getId(),
                pricing.getRoomType().getName(),
                pricing.getStartDate(),
                pricing.getEndDate(),
                pricing.getPrice());
    }

    public CreatePricingResponse(Long id, String roomType, LocalDate startDate, LocalDate endDate, BigDecimal price) {
        this.id = id;
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

}
