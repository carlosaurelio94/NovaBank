package com.mindhub.homebanking.dtos;

public class PaymentsApplicationDTO {

    private String number;
    private int cvv;
    private Double amount;
    private String description;

    public PaymentsApplicationDTO () {}

    public PaymentsApplicationDTO(String number, int cvv, Double amount, String description) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
