package com.epsilon.training.spring.banking.repositories;

import com.epsilon.training.spring.banking.entities.Customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Integer> {

    
}
