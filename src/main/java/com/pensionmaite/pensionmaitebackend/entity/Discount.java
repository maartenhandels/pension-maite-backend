package com.pensionmaite.pensionmaitebackend.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Discount {

    @Id
    private String code;

    private LocalDate startDate;

    private LocalDate endDate;

    private float discount;
}
