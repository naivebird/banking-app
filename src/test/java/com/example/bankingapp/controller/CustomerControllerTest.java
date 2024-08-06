package com.example.bankingapp.controller;

import com.example.bankingapp.model.Customer;
import com.example.bankingapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setCustomerNumber(100);
        customer1.setCustomerName("John Doe");
        customer1.setSavingsType("Savings-Deluxe");
        customer1.setCustomerDeposit(1000);
        customer1.setNumberOfYears(5);

        customer2 = new Customer();
        customer2.setId(2L);
        customer2.setCustomerNumber(101);
        customer2.setCustomerName("Jane Smith");
        customer2.setSavingsType("Savings-Regular");
        customer2.setCustomerDeposit(2000);
        customer2.setNumberOfYears(3);
    }

    @Test
    public void testListCustomers() throws Exception {
        List<Customer> customers = new ArrayList<>();
        customers.add(customer1);
        customers.add(customer2);

        when(customerRepository.findAll()).thenReturn(customers);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("customers"))
                .andExpect(model().attributeExists("customers"))
                .andExpect(model().attribute("customers", customers));

        verify(customerRepository, times(1)).findAll();
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void testShowAddForm() throws Exception {
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-customer"))
                .andExpect(model().attributeExists("customer"));
    }

    @Test
    public void testAddCustomer() throws Exception {
        when(customerRepository.findByCustomerNumber(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/save-add")
                        .param("customerNumber", "102")
                        .param("name", "John Doe")
                        .param("savingsType", "Savings-Deluxe")
                        .param("customerDeposit", "1000")
                        .param("numberOfYears", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(customerRepository, times(1)).findByCustomerNumber(102);
        verify(customerRepository, times(1)).save(any(Customer.class));
        verifyNoMoreInteractions(customerRepository);

    }

    @Test
    public void testEditCustomer() throws Exception {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer1);

        mockMvc.perform(post("/save-edit")
                        .param("id", "1")
                        .param("customerNumber", "12345")
                        .param("name", "John Doe")
                        .param("savingsType", "Savings-Deluxe")
                        .param("customerDeposit", "1000")
                        .param("numberOfYears", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(customerRepository, times(1)).save(any(Customer.class));
        verifyNoMoreInteractions(customerRepository);

    }

    @Test
    public void testShowEditForm() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-customer"))
                .andExpect(model().attributeExists("customer"))
                .andExpect(model().attribute("customer", customer1));

        verify(customerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        mockMvc.perform(get("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).delete(customer1);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void testProjectInvestment() throws Exception {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer1));

        mockMvc.perform(get("/project-investment/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("projected-investment"))
                .andExpect(model().attributeExists("projectedInvestments", "customer"))
                .andExpect(model().attribute("customer", customer1));

        verify(customerRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(customerRepository);
    }
}
