package com.example.ebankbackend.services;

import com.example.ebankbackend.dtos.CustomerDTO;
import com.example.ebankbackend.entities.BankAccount;
import com.example.ebankbackend.entities.CurrentAccount;
import com.example.ebankbackend.entities.Customer;
import com.example.ebankbackend.entities.SavingAccount;
import com.example.ebankbackend.exceptions.BalanceNotSufficentException;
import com.example.ebankbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);
    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void transfer(String accountIdSource,String accountIdDestination,double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;

    List<BankAccount> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);
}
