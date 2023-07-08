package com.pensionmaite.pensionmaitebackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import com.pensionmaite.pensionmaitebackend.events.dto.ReservedRoomType;
import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.entity.RoomType;
import com.pensionmaite.pensionmaitebackend.model.ContactData;
import com.pensionmaite.pensionmaitebackend.repository.ReservationRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.pensionmaite.pensionmaitebackend.events.request.CreateReservationRequest;
import com.pensionmaite.pensionmaitebackend.events.response.ApiResponse;
import com.pensionmaite.pensionmaitebackend.events.response.ReservationConfirmation;
import com.pensionmaite.pensionmaitebackend.exception.DBException;
import com.pensionmaite.pensionmaitebackend.exception.InvalidRequestException;
import com.pensionmaite.pensionmaitebackend.service.ReservationService;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApiReservationControllerTest {

    @Value("${env}")
    String test;

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationRepo reservationRepo;

    @InjectMocks
    private ApiReservationController reservationController;

    @BeforeEach
    void setUp() {
        System.out.println(test);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReservation_validRequest() {
        CreateReservationRequest reservationRequest = new CreateReservationRequest();
        reservationRequest.setCheckinDate(LocalDate.parse("2023-01-10"));
        reservationRequest.setCheckoutDate(LocalDate.parse("2023-01-12"));
        reservationRequest.setRoomTypes(Map.of("DOUBLE", 1));
        reservationRequest.setContactData(new ContactData("test@gmail.com", "test", "test", "12312123"));

        Room room = new Room();
        room.setRoomType(new RoomType("DOUBLE", 2, "imageFilename.png"));

        ReservationConfirmation reservationConfirmation = new ReservationConfirmation();
        reservationConfirmation.setReservationId("ABC123");
        reservationConfirmation.setCheckinDate(reservationRequest.getCheckinDate());
        reservationConfirmation.setCheckoutDate(reservationRequest.getCheckoutDate());
        reservationConfirmation.setReservedRoomTypes(ReservedRoomType.parseRoomListToReservedRoomTypeList(Set.of(room)));
        reservationConfirmation.setContactData(reservationRequest.getContactData());

        when(reservationService.createReservation(reservationRequest)).thenReturn(reservationConfirmation);

        ApiResponse<ReservationConfirmation> expectedResponse = new ApiResponse<>();
        expectedResponse.setData(reservationConfirmation);
        expectedResponse.setStatus(HttpStatus.OK);

        ApiResponse<ReservationConfirmation> actualResponse = reservationController.createReservation(reservationRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testCreateReservation_invalidRequest() {
        CreateReservationRequest reservationRequest = new CreateReservationRequest();

        when(reservationService.createReservation(reservationRequest)).thenThrow(InvalidRequestException.class);

        ApiResponse<ReservationConfirmation> expectedResponse = new ApiResponse<>();
        expectedResponse.setStatus(HttpStatus.BAD_REQUEST);

        ApiResponse<ReservationConfirmation> actualResponse = reservationController.createReservation(reservationRequest);

        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void testCreateReservation_invalidRequestException() {
        CreateReservationRequest reservationRequest = new CreateReservationRequest();
        reservationRequest.setCheckinDate(LocalDate.parse("2023-01-10"));
        reservationRequest.setCheckoutDate(LocalDate.parse("2023-01-12"));
//        reservationRequest.setRooms(Collections.singleton(new Room()));
        reservationRequest.setContactData(new ContactData("test@gmail.com", "test", "test", "12312123"));

        when(reservationService.createReservation(reservationRequest)).thenThrow(InvalidRequestException.class);

        ApiResponse<ReservationConfirmation> expectedResponse = new ApiResponse<>();
        expectedResponse.setStatus(HttpStatus.BAD_REQUEST);

        ApiResponse<ReservationConfirmation> actualResponse = reservationController.createReservation(reservationRequest);

        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }

    @Test
    void testCreateReservation_DBError() {
        CreateReservationRequest reservationRequest = new CreateReservationRequest();
        reservationRequest.setCheckinDate(LocalDate.parse("2023-01-10"));
        reservationRequest.setCheckoutDate(LocalDate.parse("2023-01-12"));
//        reservationRequest.setRooms(Collections.singleton(new Room()));
        reservationRequest.setContactData(new ContactData("test@gmail.com", "test", "test", "12312123"));

        when(reservationService.createReservation(reservationRequest)).thenThrow(DBException.class);

        ApiResponse<ReservationConfirmation> expectedResponse = new ApiResponse<>();
        expectedResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        ApiResponse<ReservationConfirmation> actualResponse = reservationController.createReservation(reservationRequest);

        assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    }
}
