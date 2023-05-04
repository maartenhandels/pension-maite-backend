package com.pensionmaite.pensionmaitebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used to represent the contact data details for a reservation.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactData {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;
}
