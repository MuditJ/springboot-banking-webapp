package com.epsilon.training.spring.banking.repositories;

import com.epsilon.training.spring.banking.entities.Account;

import java.util.*;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

    List<Account> findByCustomerId(Integer customerId);

    
    @Modifying(flushAutomatically = true)
    @Query("UPDATE Account a set a.balance = a.balance + :amount WHERE a.id = :id")
    void incrementAccountBalance(@Param("amount") double amount, @Param("id") int id);

    @Modifying(flushAutomatically = true)
    @Query("UPDATE Account a set a.balance = a.balance - :amount WHERE a.id =:id")
    void decrementAccountBalance(@Param("amount") double amount, @Param("id") int id);


}
    
