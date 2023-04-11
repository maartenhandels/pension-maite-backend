package com.pensionmaite.pensionmaitebackend.repository;

import com.pensionmaite.pensionmaitebackend.entity.Pricing;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingRepo extends CrudRepository<Pricing, Long> {

}
