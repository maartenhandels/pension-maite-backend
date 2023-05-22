package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.events.request.AuthenticationRequest;
import com.pensionmaite.pensionmaitebackend.events.request.RegisterRequest;
import com.pensionmaite.pensionmaitebackend.events.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest registerRequest);

    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
