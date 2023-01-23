package com.pensionmaite.pensionmaitebackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;

import com.pensionmaite.pensionmaitebackend.entity.Reservation;
import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.model.ContactData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import com.pensionmaite.pensionmaitebackend.events.response.CreateReservationResponse;
import com.pensionmaite.pensionmaitebackend.exception.DBException;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReservation_validRequest() {
        CreateReservationRequest reservationRequest = new CreateReservationRequest();
        reservationRequest.setCheckinDate(LocalDate.parse("2023-01-10"));
        reservationRequest.setCheckoutDate(LocalDate.parse("2023-01-12"));
        reservationRequest.setRooms(Collections.singleton(new Room()));
        reservationRequest.setContactData(new ContactData("test@gmail.com", "test", "test", "12312123"));

        System.out.println(reservationRequest);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);

        CreateReservationResponse createReservationResponse = new CreateReservationResponse();
        createReservationResponse.setReservation(reservation);

        when(reservationService.createReservation(reservationRequest)).thenReturn(reservation);

        ApiResponse<CreateReservationResponse> expectedResponse = new ApiResponse<>();
        expectedResponse.setData(createReservationResponse);
        expectedResponse.setStatus(HttpStatus.OK);

        ApiResponse<CreateReservationResponse> actualResponse = reservationController.createReservation(reservationRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testCreateReservation_invalidRequest() {
        CreateReservationRequest reservationRequest = new CreateReservationRequest();

        when(reservationService.createReservation(reservationRequest)).thenThrow(InvalidRequestException.class);

        ApiResponse<CreateReservationResponse> expectedResponse = new ApiResponse<>();
        expectedResponse.setStatus(HttpStatus.BAD_REQUEST);

        ApiResponse<CreateReservationResponse> actualResponse = reservationController.createReservation(reservationRequest);

        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void testCreateReservation_invalidRequestException() {
        CreateReservationRequest reservationRequest = new CreateReservationRequest();
        reservationRequest.setCheckinDate(LocalDate.parse("2023-01-10"));
        reservationRequest.setCheckoutDate(LocalDate.parse("2023-01-12"));
        reservationRequest.setRooms(Collections.singleton(new Room()));
        reservationRequest.setContactData(new ContactData("test@gmail.com", "test", "test", "12312123"));

        when(reservationService.createReservation(reservationRequest)).thenThrow(InvalidRequestException.class);

        ApiResponse<CreateReservationResponse> expectedResponse = new ApiResponse<>();
        expectedResponse.setStatus(HttpStatus.BAD_REQUEST);

        ApiResponse<CreateReservationResponse> actualResponse = reservationController.createReservation(reservationRequest);

        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void testCreateReservation_DBError() {
        CreateReservationRequest reservationRequest = new CreateReservationRequest();
        reservationRequest.setCheckinDate(LocalDate.parse("2023-01-10"));
        reservationRequest.setCheckoutDate(LocalDate.parse("2023-01-12"));
        reservationRequest.setRooms(Collections.singleton(new Room()));
        reservationRequest.setContactData(new ContactData("test@gmail.com", "test", "test", "12312123"));

        when(reservationService.createReservation(reservationRequest)).thenThrow(DBException.class);

        ApiResponse<CreateReservationResponse> expectedResponse = new ApiResponse<>();
        expectedResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        ApiResponse<CreateReservationResponse> actualResponse = reservationController.createReservation(reservationRequest);

        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }
}
