package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ContactDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Contact;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.ContactRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ContactController {

    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    private ContactRepository contactRepository;

    @PostMapping("/clients/current/contacts")
    public ResponseEntity<?> createContact (@RequestParam String email, @RequestParam String nickname, @RequestParam String accountNumber,
                                            Authentication authentication) {

        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account accountContact = accountService.findByNumber(accountNumber);
        Client clientContact = clientService.findByEmail(email);

        Set<Contact> contactChecked = clientCurrent.getContact().stream()
                .filter(contact -> contact.getAccountNumber().equals(accountNumber)).collect(Collectors.toSet());

        if(clientCurrent == null) {
            return new ResponseEntity<>("PLEASE AUTHENTICATE YOUR USER", HttpStatus.FORBIDDEN);
        }
        if(accountNumber.isEmpty()) {
            return new ResponseEntity<>("MISSING ACCOUNT NUMBER", HttpStatus.FORBIDDEN);
        }
        if(accountContact == null) {
            return new ResponseEntity<>("THIS ACCOUNT DOESN'T EXIST", HttpStatus.FORBIDDEN);
        }
        if(nickname.isEmpty()) {
            return new ResponseEntity<>("MISSING NICKNAME", HttpStatus.FORBIDDEN);
        }
        if(email.isEmpty()) {
            return new ResponseEntity<>("MISSING EMAIL", HttpStatus.FORBIDDEN);
        }
        if(clientContact == null) {
            return new ResponseEntity<>("THIS CLIENT DOESN'T EXIST", HttpStatus.FORBIDDEN);
        }
        if(contactChecked.size() >= 1) {
            return new ResponseEntity<>("THIS CONTACT IS ALREADY SAVED", HttpStatus.FORBIDDEN);
        }

        contactRepository.save(new Contact(accountNumber, nickname, email, clientCurrent));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/contacts")
    public Set<ContactDTO> getContact(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName())).getContact();
    }

}
