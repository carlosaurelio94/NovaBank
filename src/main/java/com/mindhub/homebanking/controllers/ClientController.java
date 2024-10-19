package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
//import com.mindhub.homebanking.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin()
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;

//    @Autowired
//    EmailService emailService;


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO() {
        return clientService.getClientsDTO();
    }

//    @RequestMapping("clients/{id}")
//    public ClientDTO getClient(@PathVariable Long id){
//        return clientService.getClientDTO();
//    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {


        if (firstName.isEmpty()) {
            return new ResponseEntity<>("MISSING FIRST NAME", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty()) {
            return new ResponseEntity<>("MISSING LAST NAME", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("MISSING EMAIL", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("MISSING PASSWORD", HttpStatus.FORBIDDEN);
        }
        if (!email.contains("@") || !email.contains(".")) {
            return new ResponseEntity<>("WE NEED A CORRECT EMAIL", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("EMAIL ALREADY IN USE", HttpStatus.FORBIDDEN);
        }



//        emailService.send(email, "Welcome to Nova Bank", "Welcome to Nova Bank " + firstName + " " + lastName +
//                " ,we are glad that you trust us. Thank you. " + "http://localhost:8080/web/accounts.html");

        clientService.saveClient(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        accountService.saveAccount(new Account("VIN" + getRandomNumber(10000000, 100000000), LocalDateTime.now(), 0.00,
                clientService.findByEmail(email)));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current")
    public ResponseEntity<?> editPassword(Authentication authentication, @RequestParam String password) {

        Client clientCurrent = clientService.findByEmail(authentication.getName());

        if (clientCurrent == null) {
            return new ResponseEntity<>("PLEASE AUTHENTICATE FIRST", HttpStatus.FORBIDDEN);

        }
        if(password.isEmpty()) {
            return new ResponseEntity<>("ENTER A NEW PASSWORD", HttpStatus.FORBIDDEN);
        }

        clientCurrent.setPassword(passwordEncoder.encode(password));
        clientService.saveClient(clientCurrent);
        return new ResponseEntity<>("CHANGE SUCCESFULLY COMPLETED", HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientDTO(Authentication authentication) {
        Client clientCurrent = clientService.findByEmail(authentication.getName());
        return new ClientDTO(clientCurrent);
    }


//
//    @PostMapping("/message")
//    @EventListener()
//    public String sendMail(@RequestParam String email) {
//        return "Message sent";
//    }




}
