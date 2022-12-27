package com.mindhub.homebanking.models;

import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

//    @MockBean
//    private PasswordEncoder passwordEncoder;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;


    @Test
    public void max3accounts(){
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        Set<Account> accountList = client.getAccount();
        assertThat(accountList.size(), lessThanOrEqualTo(3));
    }

    @Test
    public void existAdmin() {
        Client client = clientRepository.findByEmail("admin@mindhub.com");
        assertThat(client.getEmail(), containsString("admin"));
    }

    @Test
    public void ClientWith2Cards() {
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        assertThat(client.getCard().size(), equalTo(2));
    }

    @Test
    public void accountVIN008WithLessThan5000() {
        Account VIN008 = accountRepository.findByNumber("VIN008");
        assertThat(VIN008.getBalance(), lessThan(5000.00));
    }

    @Test
    public void existAccountVIN002() {
        List<String> accountList = accountRepository.findAll().stream().map(account -> account.getNumber()).collect(Collectors.toList());
        assertThat(accountList, hasItem("VIN002"));
    }

    @Test
    public void existCardColor(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("color",is(CardColor.TITANIUM))));
    }

    @Test
    public void previousCardsCreatedBeforeToday() {
        Card card = cardRepository.findByNumber("4578-2145-8587-7452");
        assertThat(card.getFromDate(), lessThan(LocalDate.now()));
    }

    @Test
    public void cardHasClient() {
        Card card = cardRepository.findByNumber("4578-2145-8587-7452");
        assertThat(card.getAccount().getClient().getFirstName(), containsString("Melba"));
    }

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existTransactionTypeCredit(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.CREDIT))));
    }

    @Test
    public void existDescriptionFerreteria() {
        List<String> transactionDescriptions = transactionRepository.findAll().stream().map(transaction -> transaction.getDescription()).collect(Collectors.toList());
        assertThat(transactionDescriptions, hasItem("Ferretería"));
    }










}
