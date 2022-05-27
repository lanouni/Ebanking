package com.example.ebankbackend.repositry;

import com.example.ebankbackend.entities.AccountOperation;
import com.example.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
}
