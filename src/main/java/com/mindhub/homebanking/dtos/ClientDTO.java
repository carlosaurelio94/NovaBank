package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Contact;

import java.util.*;
import java.util.stream.Collectors;


public class ClientDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> account = new HashSet<>();
    private List<ClientLoanDTO> loan = new ArrayList<>();
    private Set<CardDTO> card = new HashSet<>();
    private Set<ContactDTO> contact = new HashSet<>();



    public ClientDTO() {}

    public ClientDTO(Client client) {
        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.account = client.getAccount().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());

        this.loan = client.getClientLoan().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());

        this.card = client.getCard().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());

        this.contact = client.getContact().stream().map(contact -> new ContactDTO(contact)).collect(Collectors.toSet());

    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccount() {
        return account;
    }

    public List<ClientLoanDTO> getLoan() {
        return loan;
    }

    public Set<CardDTO> getCard() {
        return card;
    }

    public Set<ContactDTO> getContact() {
        return contact;
    }
}
