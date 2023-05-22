package com.pensionmaite.pensionmaitebackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "discount")
public class Discount {

    @Id
    private String code;

    private LocalDate startDate;

    private LocalDate endDate;

    private float discount;
}
