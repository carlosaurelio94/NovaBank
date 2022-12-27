package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class TransactionController {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<?> transactions(@RequestParam Double amount, @RequestParam String description,
                                          @RequestParam String numberFrom, @RequestParam String numberTo, Authentication authentication) {

        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account accountFrom = accountService.findByNumber(numberFrom);
        Account accountTo = accountService.findByNumber(numberTo);

        if (clientCurrent == null) {
            return new ResponseEntity<>("PLEASE AUTHENTICATE FIRST", HttpStatus.FORBIDDEN);

        }
        if(numberFrom.isEmpty()) {
            return new ResponseEntity<>("MISSING SOURCE ACCOUNT", HttpStatus.FORBIDDEN);
        }
        if(accountFrom == null) {
            return new ResponseEntity<>("THIS ACCOUNT DOESN´T BELONG TO THIS CLIENT", HttpStatus.FORBIDDEN);
        }
        if(accountFrom.getBalance() <= amount) {
            return new ResponseEntity<>("INSUFFICIENT BALANCE", HttpStatus.FORBIDDEN);
        }
        if(numberTo.isEmpty()){
            return new ResponseEntity<>("MISSING DESTINATION ACCOUNT", HttpStatus.FORBIDDEN);
        }
        if (accountTo == null) {
            return new ResponseEntity<>("THIS ACCOUNT DOESN'T EXIST", HttpStatus.FORBIDDEN);
        }
        if(accountFrom.getNumber() == accountTo.getNumber()) {
            return new ResponseEntity<>("THE SOURCE ACCOUNT AND THE DESTINATION ACCOUNT ARE THE SAME", HttpStatus.FORBIDDEN);
        }
        if(!accountFrom.getClient().getEmail().equals(clientCurrent.getEmail())) {
            return new ResponseEntity<>("THIS ACCOUNT DOESN´T BELONG TO THIS CLIENT 2", HttpStatus.FORBIDDEN);
        }
        if(amount <= 0) {
            return new ResponseEntity<>("MISSING AMOUNT", HttpStatus.FORBIDDEN);
        }

        if(description.isEmpty()) {
            return new ResponseEntity<>("MISSING DESCRIPTION", HttpStatus.FORBIDDEN);
        }

        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountService.saveAccount(accountFrom);
        accountTo.setBalance(accountTo.getBalance() + amount);
        accountService.saveAccount(accountTo);


        transactionService.saveTransaction(new Transaction(TransactionType.DEBIT, -amount,
                description + " " + "to:" + accountTo.getNumber(), LocalDateTime.now(),accountFrom, accountFrom.getBalance()));
        transactionService.saveTransaction(new Transaction(TransactionType.CREDIT, amount, description + " " + "from:" + accountFrom.getNumber(),
                LocalDateTime.now(),accountTo, accountTo.getBalance()));



        return new ResponseEntity<>("CHANGE SUCCESFULLY COMPLETED", HttpStatus.CREATED);
    }


    @GetMapping("/transactions")
    public List<TransactionDTO> getTransactionsDTO() {
        return transactionService.getTransactionsDTO();
    }


}
