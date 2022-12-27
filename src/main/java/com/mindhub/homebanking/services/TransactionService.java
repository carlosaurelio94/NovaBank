package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface TransactionService {

    void saveTransaction(Transaction transaction);

    public List<TransactionDTO> getTransactionsDTO();


    }
