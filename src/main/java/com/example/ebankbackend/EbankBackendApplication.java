package com.example.ebankbackend;

import com.example.ebankbackend.dtos.CustomerDTO;
import com.example.ebankbackend.entities.*;
import com.example.ebankbackend.enums.AccountStatus;
import com.example.ebankbackend.enums.OperationType;
import com.example.ebankbackend.exceptions.BalanceNotSufficentException;
import com.example.ebankbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend.exceptions.CustomerNotFoundException;
import com.example.ebankbackend.repositry.AccountOperationRepository;
import com.example.ebankbackend.repositry.BankAccountRepository;
import com.example.ebankbackend.repositry.CustomerRepository;
import com.example.ebankbackend.services.BankAccountService;
import com.example.ebankbackend.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Souhayl", "Imane", "mohammed").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,9000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*12000,5.5,customer.getId());
                    List<BankAccount> bankAccounts  = bankAccountService.bankAccountList();
                    for (BankAccount bankAccount : bankAccounts){
                        for (int i = 0; i<10;i++)
                            bankAccountService.credit(bankAccount.getId(),10000+Math.random()*120000,"Credit");
                            bankAccountService.debit(bankAccount.getId(),1000+Math.random()*9000,"Debit");

                    }

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
                catch (BankAccountNotFoundException | BalanceNotSufficentException e) {
                    e.printStackTrace();
                }
            });
        };
    }

    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });
            customerRepository.findAll().forEach(cust->{
                CurrentAccount currentAccount=new CurrentAccount ();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance (Math.random()*90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount=new SavingAccount ();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance (Math.random()*90000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });
            bankAccountRepository.findAll(). forEach(acc->{
                for (int i=0;i<10; i++){
                    AccountOperation accountOperation=new AccountOperation();
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount (Math.random()*12000);
                    accountOperation.setType(Math.random()>8.5? OperationType.DEBIT: OperationType.CREDIT);
                    accountOperation.setBankAccount (acc);
                    accountOperationRepository.save(accountOperation);
                }
            });


        };
    }
}
