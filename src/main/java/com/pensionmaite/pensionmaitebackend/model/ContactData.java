package com.pensionmaite.pensionmaitebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactData {

    private String email;

    private String firstName;

    private String lastName;

    private String phone;
}
