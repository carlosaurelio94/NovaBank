package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class AccountController {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GetMapping("/accounts")
   public  List<AccountDTO> getAccountsDTO() {
        return accountService.getAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id, Authentication authentication){

        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account accountToConsult = accountService.getAccount(id);

        if(clientCurrent.getAccount().contains(accountToConsult)) {
            AccountDTO account1 = accountService.getAccountDTO(id);
            return new ResponseEntity<>(account1, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("THIS IS NOT YOUR ACCOUNT", HttpStatus.FORBIDDEN);
    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccounts(Authentication authentication){

        Client clientCurrent = clientService.findByEmail(authentication.getName());

        if (clientCurrent == null) {
            return new ResponseEntity<>("PLEASE AUTHENTICATE FIRST", HttpStatus.FORBIDDEN);

        }
        if(clientCurrent.getAccount().size() >= 3) {
            return new ResponseEntity<>("ACCOUNT LIMIT ALREADY REACHED", HttpStatus.FORBIDDEN);
        }
        accountService.saveAccount(new Account("VIN"+getRandomNumber(10000000,100000000), LocalDateTime.now(), 0.00, clientCurrent));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    public Set<AccountDTO> getAccounts(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName())).getAccount();
    }

}
