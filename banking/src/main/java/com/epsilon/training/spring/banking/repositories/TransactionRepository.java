package com.epsilon.training.spring.banking.repositories;

import java.util.List;

import com.epsilon.training.spring.banking.entities.Transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    List<Transaction> findByAccountId(Integer accountId);
    
}
