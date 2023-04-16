package com.pensionmaite.pensionmaitebackend.events.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Data
public class CreateDefaultPricingRequest {

    private String roomType;

    private BigDecimal price;

    public static Optional<List<String>> validate(CreateDefaultPricingRequest createDefaultPricingRequest) {

        if (createDefaultPricingRequest == null) {
            Optional.of(Collections.singletonList("Request object can not be empty"));
        }

        if (StringUtils.isBlank(createDefaultPricingRequest.getRoomType())) {
            return Optional.of(Collections.singletonList("Room Type can not be empty"));
        }

        if (createDefaultPricingRequest.getRoomType() == null) {
            return Optional.of(Collections.singletonList("Price can not be empty"));
        }

        return Optional.empty();
    }
}
