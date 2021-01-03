package com.epsilon.training.spring.banking.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.*;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; 

    private String name;

    private String pan;

    //Each customer can have many accounts. Each account held
    //by only one customer -> Many to one relationship
    @OneToMany(mappedBy = "customer")
    private List<Account> accounts;

    public Customer(String name, String pan) {
        this.name = name;
        this.pan = pan;
    }

    public Customer() {

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPan() {
        return this.pan;
    }

    public void  setPan(String pan) {
        this.pan = pan;
    }


    
}
