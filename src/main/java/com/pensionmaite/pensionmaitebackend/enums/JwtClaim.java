package com.pensionmaite.pensionmaitebackend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JwtClaim {
    ROLE("role");

    @Getter
    private final String claimName;
}
