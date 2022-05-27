package com.example.ebankbackend.exceptions;

import com.example.ebankbackend.entities.BankAccount;

public class BankAccountNotFoundException extends Exception {
    public BankAccountNotFoundException(String message){
        super(message);
    }
}