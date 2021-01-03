package com.epsilon.training.spring.banking.entities;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name="account_id", nullable=false)
    private Account account;

    private LocalDateTime transactionTime;

    private double transactionAmount;

    private boolean isDeposit;

    public Transaction() {

    }

    public Transaction(LocalDateTime dt, double amt, boolean isDeposit, Account account) {
        this.transactionTime = dt;
        this.transactionAmount = amt;
        this.isDeposit = isDeposit;
        this.account = account;
    }

    public LocalDateTime getTransactionTime() {
        return this.transactionTime;
    }

    public void setTransactionTime(LocalDateTime dt) {
        this.transactionTime = dt;
    }

    public double getTransactionAmount() {
        return this.transactionAmount;
    }

    public void setTransactionAmount(double transactionAmt) {
        this.transactionAmount = transactionAmt;
    }

    public boolean getIsDeposit() {
        return this.isDeposit;
    }

    public void setIsDeposit(boolean isDeposit){
        this.isDeposit = isDeposit;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
