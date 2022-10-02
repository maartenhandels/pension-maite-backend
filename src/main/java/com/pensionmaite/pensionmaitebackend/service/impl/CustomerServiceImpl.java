package com.pensionmaite.pensionmaitebackend.service.impl;

import com.pensionmaite.pensionmaitebackend.entity.Customer;
import com.pensionmaite.pensionmaitebackend.repository.CustomerRepo;
import com.pensionmaite.pensionmaitebackend.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepo customerRepo;

    @Override
    public Customer getCustomer(Long customerId) {
        return customerRepo.findById(customerId).orElse(null);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepo.save(customer);
    }
}
