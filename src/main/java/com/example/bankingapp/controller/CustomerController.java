package com.example.bankingapp.controller;

import com.example.bankingapp.dto.ProjectedInvestmentDto;
import com.example.bankingapp.model.Customer;
import com.example.bankingapp.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/")
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "customers";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add-customer";
    }

    @PostMapping("/save-add")
    public String addCustomer(@ModelAttribute Customer customer, Model model) {
        // Display an error if the customer number already exists
        if (customerRepository.findByCustomerNumber(customer.getCustomerNumber()).isPresent()) {
            model.addAttribute("error", "The record you are trying to add is already existing. Choose a different customer number");
            return listCustomers(model);
        }
        customerRepository.save(customer);
        return "redirect:/";
    }

    @PostMapping("/save-edit")
    public String editCustomer(@ModelAttribute Customer customer) {
        customerRepository.save(customer);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        model.addAttribute("customer", customer);
        return "edit-customer";
    }


    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable("id") long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        customerRepository.delete(customer);
        return "redirect:/";
    }

    @GetMapping("/project-investment/{id}")
    public String projectInvestment(@PathVariable("id") long id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer Id:" + id));
        List<ProjectedInvestmentDto> projectedInvestments = new ArrayList<>();
        double interestRate;
        if (Objects.equals(customer.getSavingsType(), "Savings-Deluxe")) {
            interestRate = 0.15;
        } else {
            interestRate = 0.10;
        }
        double principal = customer.getCustomerDeposit();
        for (int i = 1; i <= customer.getNumberOfYears(); i++) {
            double interest = principal * interestRate;
            double endingBalance = principal + interest;
            ProjectedInvestmentDto projectedInvestmentDto = new ProjectedInvestmentDto(i, principal, interest, endingBalance);
            projectedInvestments.add(projectedInvestmentDto);
            principal += interest;
        }
        model.addAttribute("projectedInvestments", projectedInvestments);
        model.addAttribute("customer", customer);

        return "projected-investment";
    }
}
