package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.services.implement.AccountServiceImplement;

import java.util.List;

public interface AccountService {

    void saveAccount(Account account);

    List<AccountDTO> getAccountsDTO();

    AccountDTO getAccountDTO(Long id);

    Account getAccount(Long id);

    Account findByNumber(String number);

    }
