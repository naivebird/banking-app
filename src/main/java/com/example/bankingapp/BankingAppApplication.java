package com.example.bankingapp;

import com.example.bankingapp.model.Customer;
import com.example.bankingapp.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class BankingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            customerRepository.save(new Customer(1L, 115, "Jasper Diaz", 15000.0, 5, "Savings-Deluxe"));
            customerRepository.save(new Customer(2L, 112, "Zanip Mendez", 5000.0, 2, "Savings-Deluxe"));
            customerRepository.save(new Customer(3L, 113, "Geromina Esper", 5000.0, 2, "Savings-Regular"));
        };
    }
}

