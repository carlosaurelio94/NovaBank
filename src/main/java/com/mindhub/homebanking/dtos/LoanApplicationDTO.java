package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private long id;
    private long loanId;
    private int amount;
    private int payments;
    private String destinationAccount;

    public LoanApplicationDTO() {}

    public LoanApplicationDTO (long id, long loanId, int amount, int payments, String destinationAccount) {
        this.id = id;
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccount = destinationAccount;
    }

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }

    public int getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }
}
