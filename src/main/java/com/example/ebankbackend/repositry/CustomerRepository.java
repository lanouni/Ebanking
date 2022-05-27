package com.example.ebankbackend.repositry;

import com.example.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
