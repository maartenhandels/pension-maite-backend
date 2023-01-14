package com.pensionmaite.pensionmaitebackend.events.request;

import com.pensionmaite.pensionmaitebackend.entity.Room;
import com.pensionmaite.pensionmaitebackend.model.ContactData;
import com.pensionmaite.pensionmaitebackend.util.EmailValidator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Data
public class CreateReservationRequest {

    private Set<Room> rooms;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private ContactData contactData;

    /**
     * Checks if the request is valid
     * @return A List of Strings with the errors, if it has any
     */
    public static List<String> validate(CreateReservationRequest createReservationRequest) {

        if (createReservationRequest == null) {
            return Collections.singletonList("Request object can not be empty");
        }

        List<String> errors = new ArrayList<>();
        validateRooms(errors, createReservationRequest.getRooms());
        validateDates(errors, createReservationRequest.getCheckinDate(), createReservationRequest.getCheckoutDate());
        validateContactData(errors, createReservationRequest.getContactData());

        return errors;
    }

    /**
     * Checks if rooms list is valid (not null)
     * @param errors List of errors where error string will be added if found
     */
    private static void validateRooms(List<String> errors, Set<Room> rooms) {
        if (CollectionUtils.isEmpty(rooms)) {
            errors.add("Rooms List can not be empty");
        }
    }

    /**
     * Checks if dates are valid
     * @param errors List of errors where error string will be added if found
     */
    private static void validateDates(List<String> errors, LocalDate checkinDate, LocalDate checkoutDate) {
        if (checkinDate == null || checkoutDate == null) {
            errors.add("Checkin and Checkout dates can not be empty");
            return;
        }

        if (!checkoutDate.isAfter(checkinDate)){
            errors.add("Checkout date must be after checkin date");
            return;
        }
    }

    /**
     * Checks if contact data is valid
     * Rules:
     * - contact data object not null
     * - email not null or empty
     * - email must be in a valid email format
     * @param errors List of errors where error string will be added if found
     */
    private static void validateContactData(List<String> errors, ContactData contactData) {
        if (contactData == null) {
            errors.add("Contact Data can not be empty");
            return;
        }

        if(StringUtils.isEmpty(contactData.getEmail())) {
            errors.add("Email can not be empty");
            return;
        }

        if (!EmailValidator.IsValidEmail(contactData.getEmail())) {
            errors.add("Email is not valid");
            return;
        }
    }
}
