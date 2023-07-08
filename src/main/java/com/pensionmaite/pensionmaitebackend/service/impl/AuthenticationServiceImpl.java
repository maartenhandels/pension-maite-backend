package com.pensionmaite.pensionmaitebackend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pensionmaite.pensionmaitebackend.auth.JwtService;
import com.pensionmaite.pensionmaitebackend.entity.User;
import com.pensionmaite.pensionmaitebackend.enums.JwtClaim;
import com.pensionmaite.pensionmaitebackend.events.request.AuthenticationRequest;
import com.pensionmaite.pensionmaitebackend.events.request.RegisterRequest;
import com.pensionmaite.pensionmaitebackend.events.response.Authentication;
import com.pensionmaite.pensionmaitebackend.repository.UserRepo;
import com.pensionmaite.pensionmaitebackend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public Authentication register(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstname())
                .lastName(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .build();
        userRepo.save(user);
        Map<String, Object> roleClaim = Map.of(JwtClaim.ROLE.getClaimName(), user.getRole().name());
        String jwtToken = jwtService.generateToken(roleClaim, user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return Authentication.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Authentication authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        User user = userRepo.findByEmail(authenticationRequest.getEmail()).orElseThrow();
        Map<String, Object> roleClaim = Map.of(JwtClaim.ROLE.getClaimName(), user.getRole().name());
        String jwtToken = jwtService.generateToken(roleClaim, user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("Access Token {}", jwtToken);
        log.info("Refresh Token {}", refreshToken);
        return Authentication.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            UserDetails user = userRepo.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                Authentication authResponse = Authentication.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
