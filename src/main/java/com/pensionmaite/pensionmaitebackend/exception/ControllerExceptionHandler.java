package com.pensionmaite.pensionmaitebackend.exception;

import com.pensionmaite.pensionmaitebackend.enums.ApiErrorCode;
import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Exception Handler for DuplicateRoomNumberException. Returns the error code: DUPLICATE_ROOM_NUMBER.
     *
     * @return returns a {@code ResponseEntity} object containing a {@code ApiResponse} object with error
     * code: DUPLICATE_ROOM_NUMBER.
     */
    @ExceptionHandler(value = DuplicateRoomNumberException.class)
    public ResponseEntity<ApiResponse<Object>> duplicateRoomNumberHandler() {
        log.info("Canceling creation of Room number due to room number not being unique.");
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        null,
                        ApiErrorCode.DUPLICATE_ROOM_NUMBER.getValue(),
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    /**
     * Exception Handler for InvalidRoomTypeException. Returns the error code: INVALID_ROOM_TYPE.
     *
     * @return returns a {@code ResponseEntity} object containing a {@code ApiResponse} object with error
     * code: INVALID_ROOM_TYPE.
     */
    @ExceptionHandler(value = InvalidRoomTypeException.class)
    public ResponseEntity<ApiResponse<Object>> invalidRoomTypeHandler() {
        log.info("Canceling creation of Room number due to unexisting room type.");
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        null,
                        ApiErrorCode.INVALID_ROOM_TYPE.getValue(),
                        HttpStatus.BAD_REQUEST
                )
        );
    }

    /**
     * Default Exception Handler. It will handle any exception not handled already by other ExceptionHandler methods.
     *
     * @return returns a {@code ResponseEntity} object containing a {@code ApiResponse} object with error
     * code: INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler
    public ResponseEntity<ApiResponse<Object>> handleDefaultException(Exception ex) {
        return ResponseEntity.internalServerError().body(
                new ApiResponse<>(
                        null,
                        ApiErrorCode.INTERNAL_SERVER_ERROR.getValue(),
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
        );
    }
}
