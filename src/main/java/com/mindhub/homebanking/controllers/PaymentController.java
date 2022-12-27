package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentsApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.jdbc.support.JdbcUtils.isNumeric;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PaymentController {

    @Autowired
    CardService cardService;

    @Autowired
    AccountService accountService;

    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<?> transactions(@RequestBody PaymentsApplicationDTO paymentsApplicationDTO) {



        String number = paymentsApplicationDTO.getNumber();
        boolean isNumeric = (number != null && number.matches("[0-9]+"));

        Integer cvv = paymentsApplicationDTO.getCvv();



        if(number.isEmpty()) {
            return new ResponseEntity<>("EMPTY CARD NUMBER", HttpStatus.FORBIDDEN);
        }

        if(!isNumeric) {
            return new ResponseEntity<>("JUST WRITE NUMBERS FOR CARD NUMBERS", HttpStatus.FORBIDDEN);
        }
        if(number.length() != 16) {
            return new ResponseEntity<>("MUST HAVE 16 NUMBERS", HttpStatus.FORBIDDEN);
        }
        if(cvv == 0) {
            return new ResponseEntity<>("EMPTY CVV", HttpStatus.FORBIDDEN);
        }
        if(cvv < 0 || cvv > 999) {
            return new ResponseEntity<>("YOU MUST USE NUMBERS BETWEEN: 001 AND 999", HttpStatus.FORBIDDEN);
        }
        if(cvv.toString().length() <3) {
            return new ResponseEntity<>("YOU MUST USE 3 NUMBERS FOR CVV", HttpStatus.FORBIDDEN);
        }
        String numberModified = number.substring(0,4) + "-" + number.substring(4,8) + "-" + number.substring(8,12) + "-" + number.substring(12,16);
        Card cardCurrent = cardService.findByNumber(numberModified);
        if(cardCurrent == null) {
            return new ResponseEntity<>("THIS CARD DOESN'T BELONG TO THIS CLIENT", HttpStatus.FORBIDDEN);
        }
        if(cardCurrent.getCvv() != cvv) {
            return new ResponseEntity<>("WRONG CVV", HttpStatus.FORBIDDEN);
        }
        if(paymentsApplicationDTO.getAmount() == null) {
            return new ResponseEntity<>("EMPTY AMOUNT", HttpStatus.FORBIDDEN);
        }
        Account accountCurrent = cardCurrent.getAccount();
        if(accountCurrent == null) {
            return new ResponseEntity<>("MISSING ACCOUNT", HttpStatus.FORBIDDEN);
        }

        LocalDate localDate = LocalDate.now();
        if (accountCurrent.getBalance() < paymentsApplicationDTO.getAmount()) {
            return new ResponseEntity<>("YOU DON'T HAVE ENOUGH MONEY", HttpStatus.FORBIDDEN);
        }
        if(paymentsApplicationDTO.getDescription().isEmpty()) {
            return new ResponseEntity<>("EMPTY DESCRIPTION", HttpStatus.FORBIDDEN);
        }


        accountCurrent.setBalance(accountCurrent.getBalance() - paymentsApplicationDTO.getAmount());
        transactionService.saveTransaction(new Transaction(TransactionType.DEBIT,
                -paymentsApplicationDTO.getAmount(), paymentsApplicationDTO.getDescription(), LocalDateTime.now(), accountCurrent, accountCurrent.getBalance()));
        accountService.saveAccount(accountCurrent);

        return new ResponseEntity<>("CHANGE SUCCESFULLY COMPLETED", HttpStatus.CREATED);
    }
}
