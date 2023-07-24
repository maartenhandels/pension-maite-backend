package com.pensionmaite.pensionmaitebackend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ApiErrorCode {
    DUPLICATE_ROOM_NUMBER("DUPLICATE_ROOM_NUMBER"),
    INVALID_ROOM_TYPE("INVALID_ROOM_TYPE"),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR");

    @Getter
    private final String value;
}
