package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoanController {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    LoanService loanService;
    @Autowired
    TransactionService transactionService;

    @Autowired
    ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public Set<LoanDTO> getLoanDTO() {
        return loanService.getLoanDTO();
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<?> createLoans(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        Client clientCurrent = clientService.findByEmail(authentication.getName());

        if (clientCurrent == null) {
            return new ResponseEntity<>("PLEASE AUTHENTICATE FIRST", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getLoanId() == 0) {
            return new ResponseEntity<>("PLEASE CHOOSE A LOAN", HttpStatus.FORBIDDEN);
        }

        Loan loanRequested = loanService.getLoan(loanApplicationDTO.getLoanId());

        if (loanRequested == null) {
            return new ResponseEntity<>("THIS LOAN DOESN'T EXIST", HttpStatus.FORBIDDEN);
        }

        Account destinationAccountExist = accountService.findByNumber(loanApplicationDTO.getDestinationAccount());

        List<Account> destinationAccount = clientCurrent.getAccount().stream().filter(account ->
                account.getNumber().contains(loanApplicationDTO.getDestinationAccount())).collect(Collectors.toList());

        List<Integer> payments = loanRequested.getPayments().stream().filter(payment ->
                payment == loanApplicationDTO.getPayments()).collect(Collectors.toList());

        Set<ClientLoan> clientLoans = clientCurrent.getClientLoan().stream().filter(clientLoan ->
                clientLoan.getLoan().getId() == loanApplicationDTO.getLoanId()).collect(Collectors.toSet());




        if (loanApplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("PLEASE CHOOSE A CORRECT AMOUNT", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() == 0) {
            return new ResponseEntity<>("PLEASE CHOOSE YOUR PAYMENTS", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getDestinationAccount().isEmpty()) {
            return new ResponseEntity<>("PLEASE CHOOSE AN ACCOUNT", HttpStatus.FORBIDDEN);
        }

        if (loanRequested.getMaxAmount() < loanApplicationDTO.getAmount()) {
            return new ResponseEntity<>("YOU CHOOSE AN AMOUNT HIGHER THAN YOU COULD", HttpStatus.FORBIDDEN);
        }
        if (payments.size() < 1) {
            return new ResponseEntity<>("YOU CHOOSE AN INCORRECT NUMBER OF PAYMENTS", HttpStatus.FORBIDDEN);
        }
        if (destinationAccountExist == null) {
            return new ResponseEntity<>("THE DESTINATION ACCOUNT DOESN'T EXIST", HttpStatus.FORBIDDEN);
        }
        if (destinationAccount.size() < 1) {
            return new ResponseEntity<>("THIS ACCOUNT DOESN'T BELONG TO THIS CLIENT", HttpStatus.FORBIDDEN);
        }
        if (clientLoans.size() > 0) {
            return new ResponseEntity<>("YOU ALREADY HAVE A LOAN OF THIS TYPE", HttpStatus.FORBIDDEN);
        }


        double amountTransaction = loanApplicationDTO.getAmount();
        double amountInterest = loanRequested.getInterestRate();
        int amount = (int) Math.round(amountTransaction * amountInterest);


        clientLoanService.saveClientLoan(new ClientLoan(clientCurrent, loanRequested, amount, loanApplicationDTO.getPayments()));
        transactionService.saveTransaction(new Transaction(TransactionType.CREDIT, amountTransaction,
                "Loan type: " + loanRequested.getName() + ". Loan approved", LocalDateTime.now(),
                destinationAccountExist, destinationAccountExist.getBalance()));
        destinationAccountExist.setBalance(destinationAccountExist.getBalance() + amountTransaction);
        accountService.saveAccount(destinationAccountExist);

        return new ResponseEntity<>("CHANGE SUCCESFULLY COMPLETED", HttpStatus.CREATED);
    }

}
