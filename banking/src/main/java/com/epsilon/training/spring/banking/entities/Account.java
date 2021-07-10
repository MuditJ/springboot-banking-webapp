package com.epsilon.training.spring.banking.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.time.*;
import java.util.*;

@Entity
public class Account {

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double balance;

    private LocalDate createdOn;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    public Account(double balance) {
        this.balance = balance;
    }

    public Account(Customer customer) {
        this.customer = customer;
    }

    public Account (double bal, LocalDate date, Customer customer) {
        this.balance = bal;
        this.createdOn = date;
        this.customer = customer;
    }

    public Account() {

    }

    public double getBalance() 
    {
        return this.balance;
    }

    public void setBalance(double bal) {
        this.balance = bal;
    }

    public LocalDate getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(LocalDate date) {
        this.createdOn = date;
    }

    public int getId() {
        return this.id;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

}
