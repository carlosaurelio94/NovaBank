package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Contact;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class ContactDTO {

    private long id;

    private String accountNumber;

    private String nickname;

    private String email;

    private Client client;

    public ContactDTO () {}

    public ContactDTO (Contact contact) {
        this.id = contact.getId();
        this.accountNumber = contact.getAccountNumber();
        this.nickname = contact.getNickname();
        this.email = contact.getEmail();
    }

    public long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

}
