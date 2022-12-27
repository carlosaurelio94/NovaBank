package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
//import com.mindhub.homebanking.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {



	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Autowired
	private PasswordEncoder passwordEncoder;

//	@Autowired
//	private EmailService emailService;




	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			Client client3 = new Client("Ema", "Leiva", "ema@gmail.com", passwordEncoder.encode("a"));
			Client client2 = new Client("Carlos", "Rodríguez", "carlos@gmail.com", passwordEncoder.encode("a"));
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("a"));
			Client client4 = new Client("María", "Domínguez", "maria@mindhub.com", passwordEncoder.encode("a"));
			Client client5 = new Client("admin", "admin", "admin@mindhub.com", passwordEncoder.encode("a"));
			Client pepito = new Client("pepito", "perez", "pepito@mindhub.com", passwordEncoder.encode("a"));

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);
			clientRepository.save(client4);
			clientRepository.save(client5);
			clientRepository.save(pepito);

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.00);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00);
			Account account3 = new Account("VIN003", LocalDateTime.now().plusMonths(1), 50000.00);
			//metodo con Constructor en parametros
			Account account4 = new Account("VIN004", LocalDateTime.now().minusMonths(4), 10000.00, client3);
			Account account5 = new Account("VIN005", LocalDateTime.now().minusMonths(3), 10000.00, client2);
			Account account6 = new Account("VIN006", LocalDateTime.now().minusMonths(5), 10000.00, client3);
			Account account7 = new Account("VIN007", LocalDateTime.now(), 50000.00, client4);
			Account account8 = new Account("VIN008", LocalDateTime.now(),852.14,client1);




			//metodo addClient en Client.java
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);
			accountRepository.save(account5);
			accountRepository.save(account6);
			accountRepository.save(account7);
			accountRepository.save(account8);


			Transaction transaction1 = new Transaction(TransactionType.DEBIT, -200.00, "Ferretería", LocalDateTime.now().plusDays(15));
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -500.00, "Renta telefónica", LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, -300.00, "Zapatería", LocalDateTime.now().minusDays(15));
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -1500.00, "Seguro", LocalDateTime.now());
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, -1000.00, "Hamburguesería", LocalDateTime.now());
			Transaction transaction8 = new Transaction(TransactionType.DEBIT, -5140.00, "Alquiler", LocalDateTime.now());
			//metodo con constructor en parametros
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 3500.00, "Salario", LocalDateTime.now().plusMinutes(1), account1, account1.getBalance());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 700.00, "Reintegro", LocalDateTime.now().plusMinutes(1), account2, account2.getBalance());
			Transaction transaction9 = new Transaction(TransactionType.CREDIT, 350.00, "Pagos", LocalDateTime.now().plusMinutes(1), account3, account3.getBalance());
			Transaction transaction10 = new Transaction(TransactionType.CREDIT, 700.00, "Retorno", LocalDateTime.now().plusMinutes(1), account4, account4.getBalance());
			Transaction transaction11 = new Transaction(TransactionType.CREDIT, 350.00, "Auxilio", LocalDateTime.now().plusMinutes(1), account5, account5.getBalance());
			Transaction transaction12 = new Transaction(TransactionType.CREDIT, 700.00, "Devolución", LocalDateTime.now().plusMinutes(1), account6, account6.getBalance());


			//metodo addAccount en Account.java
			account1.addTransaction(transaction1);
			account4.addTransaction(transaction4);
			account2.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account5.addTransaction(transaction7);
			account6.addTransaction(transaction8);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);
			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);

			System.out.println(transaction1.toString());

			Loan loan1 = new Loan("Mortgage", 500000, List.of(12,24,36,48,60), 1.4);
			Loan loan2 = new Loan("Personal", 100000, List.of(6,12,24), 1.2);
			Loan loan3 = new Loan("Automobile", 300000, List.of(6,12,24,36), 1.3);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 400000, 60);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, 50000, 12);
			ClientLoan clientLoan3 = new ClientLoan(client2, loan2, 100000, 36);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan3, 200000, 36);
			ClientLoan clientLoan5 = new ClientLoan(client3, loan1, 1420000, 48);
			ClientLoan clientLoan6 = new ClientLoan(client3, loan3, 87400, 6);



			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
			clientLoanRepository.save(clientLoan5);
			clientLoanRepository.save(clientLoan6);

			Card card1 = new Card(client1.getFirstName()+ " " + client1.getLastName(), CardType.DEBIT, CardColor.GOLD, CardValidate.OK, "4578-2145-8587-7452", 452, LocalDate.now().minusDays(1), LocalDate.now().minusYears(5), client1, account1);
			Card card2 = new Card(client1.getFirstName()+ " " + client1.getLastName(), CardType.CREDIT, CardColor.TITANIUM, CardValidate.OK, "4578-5214-7412-2746", 894, LocalDate.now().plusYears(5), LocalDate.now(), client1, account2);
			Card card3 = new Card(client2.getFirstName()+ " " + client2.getLastName(), CardType.CREDIT, CardColor.SILVER, CardValidate.OK, "4578-9716-2896-5139", 736, LocalDate.now().plusYears(5), LocalDate.now(), client2, account3);
			Card card4 = new Card(client3.getFirstName()+ " " + client3.getLastName(), CardType.DEBIT, CardColor.TITANIUM, CardValidate.OK, "4578-8139-1637-1687", 215, LocalDate.now().plusYears(5), LocalDate.now(), client3, account4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);
			cardRepository.save(card4);


		};

	}

}
