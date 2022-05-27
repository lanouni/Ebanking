package com.example.ebankbackend.web;

import com.example.ebankbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

//    @GetMapping("/accounts/{accountId}")
//    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
//        return bankAccountService.getBankAccount(accountId);
//    }
//    @GetMapping("/accounts")
//    public List<BankAccountDTO> listAccounts(){
//        return bankAccountService.bankAccountList();
//    }
}