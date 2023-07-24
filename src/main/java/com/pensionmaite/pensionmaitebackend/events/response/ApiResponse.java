package com.pensionmaite.pensionmaitebackend.events.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private T data;

    private String error;

    private HttpStatus status;

    public ApiResponse(T data) {
        this.data = data;
    }
}
