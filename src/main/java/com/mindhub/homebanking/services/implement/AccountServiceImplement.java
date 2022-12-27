package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public List<AccountDTO> getAccountsDTO(){
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }

    public AccountDTO getAccountDTO(Long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }
}
