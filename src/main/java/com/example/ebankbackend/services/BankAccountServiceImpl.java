package com.example.ebankbackend.services;

import com.example.ebankbackend.dtos.CustomerDTO;
import com.example.ebankbackend.entities.*;
import com.example.ebankbackend.enums.OperationType;
import com.example.ebankbackend.exceptions.BalanceNotSufficentException;
import com.example.ebankbackend.exceptions.BankAccountNotFoundException;
import com.example.ebankbackend.exceptions.CustomerNotFoundException;
import com.example.ebankbackend.mappers.BankAccountMapperImpl;
import com.example.ebankbackend.repositry.AccountOperationRepository;
import com.example.ebankbackend.repositry.BankAccountRepository;
import com.example.ebankbackend.repositry.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("saving new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null){
            throw new CustomerNotFoundException("customer not found Exception");
        }
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount save = bankAccountRepository.save(currentAccount);

        return save;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return savedBankAccount;
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers =  customerRepository.findAll();
        List<CustomerDTO> customerDTOS =customers.stream().map(cust->dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("bankAccount not found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount= getBankAccount(accountId);
        System.out.println("************** "+bankAccount.getId());
        if (bankAccount.getBalance()<amount)
            throw new BalanceNotSufficentException("balance not sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount= getBankAccount(accountId);

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
        debit(accountIdSource,amount,"transfer to "+ accountIdDestination);
        credit(accountIdDestination,amount,"transfer from "+ accountIdSource);
    }

    @Override
    public  List<BankAccount> bankAccountList(){
        return  bankAccountRepository.findAll();
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("saving new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer saveCustomer= customerRepository.save(customer);
        return dtoMapper.fromCustomer(saveCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }
}