package com.mindhub.homebanking.models;

import net.minidev.json.JSONUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SourceType;

import javax.persistence.*;
import javax.persistence.spi.PersistenceUnitTransactionType;
import java.time.LocalDateTime;



@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private Long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;

    private Double balance;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    public Transaction () {}

    public Transaction (TransactionType type, Double amount, String description, LocalDateTime date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Transaction (TransactionType type, Double amount, String description, LocalDateTime date, Account account, Double balance) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
        this.balance = account.getBalance();
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", typer='" + type + '\'' +
                ", amount='" + amount + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                '}';

    }


}
