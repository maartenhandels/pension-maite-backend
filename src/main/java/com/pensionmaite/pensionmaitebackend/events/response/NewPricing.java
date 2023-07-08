package com.pensionmaite.pensionmaitebackend.events.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class NewPricing {

    private Long id;

    private String roomType;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal price;

    public NewPricing(com.pensionmaite.pensionmaitebackend.entity.Pricing pricing) {
        this(pricing.getId(),
                pricing.getRoomType().getName(),
                pricing.getStartDate(),
                pricing.getEndDate(),
                pricing.getPrice());
    }

    public NewPricing(Long id, String roomType, LocalDate startDate, LocalDate endDate, BigDecimal price) {
        this.id = id;
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

}
