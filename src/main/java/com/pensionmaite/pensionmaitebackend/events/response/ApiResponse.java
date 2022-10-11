package com.pensionmaite.pensionmaitebackend.events.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ApiResponse<T> {

    private T data;

    private String error;

    private HttpStatus status;
}
