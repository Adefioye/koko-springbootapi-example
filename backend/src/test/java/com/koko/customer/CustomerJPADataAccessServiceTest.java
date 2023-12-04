package com.koko.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    @Mock private CustomerRepository customerRepository;
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // When
        underTest.selectAllCustomers();
        // Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.selectCustomerById(id);
        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void removeCustomerById() {
        // Given
        int id = 1;
        // When
        underTest.removeCustomerById(id);
        // Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
        Customer customer = new Customer(
                1,
                "koko",
                "koko@example.com",
                "password", Gender.MALE, 65
        );
        // When
        underTest.updateCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void insertCustomer() {
        // Given
        Customer customer = new Customer(
                1,
                "koko",
                "koko@example.com",
                "password", Gender.MALE, 65
        );
        // When
        underTest.insertCustomer(customer);
        // Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsCustomerEmail() {
        // Given
        String email = "koko@example.com";
        // When
        underTest.existsCustomerEmail(email);
        // Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsCustomerId() {
        // Given
        int id = 1;
        // When
        underTest.existsCustomerId(id);
        // Then
        verify(customerRepository).existsCustomerById(id);
    }
}