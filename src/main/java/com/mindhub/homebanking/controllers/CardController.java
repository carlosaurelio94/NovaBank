package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

//import static com.mindhub.homebanking.utils.CardUtils.getCvv;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class CardController {



    @Autowired
    ClientService clientService;

    @Autowired
    AccountService accountService;
    @Autowired
    CardService cardService;

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String card() {
        return getRandomNumber(1000, 10000) +
                "-" + getRandomNumber(1000, 10000)+
                "-" + getRandomNumber(1000, 10000) +
                "-" + getRandomNumber(1000, 10000);
    }

    
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardType type, @RequestParam CardColor color, @RequestParam String number, Authentication authentication) {

        Client clientCurrent = clientService.findByEmail(authentication.getName());
        if(clientCurrent == null) {
            return new ResponseEntity<>("PLEASE AUTHENTICATE YOUR USER", HttpStatus.FORBIDDEN);
        }

        Account accountActive = accountService.findByNumber(number);
        if(accountActive == null) {
            return new ResponseEntity<>("THIS ACCOUNT DOESN'T EXIST", HttpStatus.FORBIDDEN);
        }

        Set<Account> accountSet = clientCurrent.getAccount();
        Set<Account> accountCurrent = accountSet.stream().filter(account -> account.getNumber().equals(number)).collect(Collectors.toSet());
        if(accountCurrent.size() < 1) {
            return new ResponseEntity<>("THIS ACCOUNT DOESN'T BELONG TO THIS CLIENT", HttpStatus.FORBIDDEN);
        }

        Set<Card> cardsTotal = clientCurrent.getCard().stream().filter(card -> card.getValidate() == CardValidate.OK).collect(Collectors.toSet());
        Set<Card> cards = clientCurrent.getCard().stream()
                .filter(card -> card.getType() == type && card.getValidate() == CardValidate.OK).collect(Collectors.toSet());
        Set<Card> cardColor = cards.stream().filter(card -> card.getColor() == color).collect(Collectors.toSet());

        if (cardsTotal.size() >= 6) {
            return new ResponseEntity<>("THE MAXIMUM NUMBER OF CARDS HAS ALREADY BEEN REACHED", HttpStatus.FORBIDDEN);
        }
        if (cards.size() >= 3){
            return new ResponseEntity<>("THE MAXIMUM NUMBER OF" + " " + type + " " + "CARDS HAS ALREADY BEEN REACHED", HttpStatus.FORBIDDEN);
        }
        if(cardColor.size() >= 1){
            return new ResponseEntity<>("THE MAXIMUM NUMBER OF" + " " + type + " " + color + " " + "CARDS HAS ALREADY BEEN REACHED", HttpStatus.FORBIDDEN);
        }



        String cardNumber = CardUtils.getCardNumber();

        int cvv = CardUtils.getCvv();

        Card cardCurrent = new Card(clientCurrent.getFirstName() + " " + clientCurrent.getLastName(),
                type, color, CardValidate.OK, cardNumber, cvv, LocalDate.now().plusYears(5),
                LocalDate.now(), clientCurrent, accountActive);

        cardService.saveCard(cardCurrent);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }




    @PatchMapping("/cards")
    public ResponseEntity<Object> deleteCard(@RequestParam String number, Authentication authentication) {
        Client clientCurrent = clientService.findByEmail(authentication.getName());
        if(clientCurrent == null) {
            return new ResponseEntity<>("PLEASE AUTHENTICATE YOUR USER", HttpStatus.FORBIDDEN);
        }
        Card cardCurrent = cardService.findByNumber(number);
        if(cardCurrent == null) {
            return new ResponseEntity<>("THIS CARD DOESN'T EXIST", HttpStatus.FORBIDDEN);
        }
        Set<Card> cards = clientCurrent.getCard().stream().filter(card -> card.getNumber().equals(number)).collect(Collectors.toSet());
        if(cards.size() < 1) {
            return new ResponseEntity<>("THIS CARD DOESN'T BELONG TO THIS CLIENT", HttpStatus.FORBIDDEN);
        }
        if(cardCurrent.getValidate() == CardValidate.DELETE) {
            return new ResponseEntity<>("THIS CARD WAS ALREADY DELETED", HttpStatus.FORBIDDEN);
        }



        cardCurrent.setValidate(CardValidate.DELETE);
        cardService.saveCard(cardCurrent);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}



//    @GetMapping("/clients/current/accounts")
//    public Set<AccountDTO> getAll(Authentication authentication) {
//        return new ClientDTO(clientRepository.findByEmail (authentication.getName())).getAccount();
//    }

