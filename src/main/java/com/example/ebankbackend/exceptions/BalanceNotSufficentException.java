package com.example.ebankbackend.exceptions;

public class BalanceNotSufficentException extends Throwable {
    public BalanceNotSufficentException(String s) {
        super(s);
    }
}