package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.events.request.AuthenticationRequest;
import com.pensionmaite.pensionmaitebackend.events.request.RegisterRequest;
import com.pensionmaite.pensionmaitebackend.events.response.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    Authentication register(RegisterRequest registerRequest);

    Authentication authenticate(AuthenticationRequest authenticationRequest);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
