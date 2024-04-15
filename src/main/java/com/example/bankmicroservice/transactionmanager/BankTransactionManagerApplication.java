package com.example.bankmicroservice.transactionmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BankTransactionManagerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(BankTransactionManagerApplication.class, args);
	}
}
