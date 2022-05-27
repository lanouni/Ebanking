package com.example.ebankbackend.repositry;

import com.example.ebankbackend.entities.BankAccount;
import com.example.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,String> {
}
