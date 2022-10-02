package com.pensionmaite.pensionmaitebackend.service;

import com.pensionmaite.pensionmaitebackend.entity.Customer;

import java.util.Optional;

public interface CustomerService {

    Customer getCustomer(Long customerId);

    Customer saveCustomer(Customer customer);
}
