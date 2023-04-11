package com.pensionmaite.pensionmaitebackend.events.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class CreatePricingRequest {

    private String roomType;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal price;

    public static Optional<List<String>> validate(CreatePricingRequest createPricingRequest) {

        if (createPricingRequest == null) {
            Optional.of(Collections.singletonList("Request object can not be empty"));
        }

        if (StringUtils.isBlank(createPricingRequest.roomType)) {
            return Optional.of(Collections.singletonList("Room Type can not be empty"));
        }

        if (createPricingRequest.isOnlyOneDateEmpty()) {
            return Optional.of(Collections.singletonList("Either both dates must have a value or both must be empty"));
        }

        return Optional.empty();
    }

    private boolean isOnlyOneDateEmpty() {
        if ((this.startDate == null && this.endDate != null) || (this.startDate != null && this.endDate == null)) {
            return true;
        }
        return false;
    }

}
