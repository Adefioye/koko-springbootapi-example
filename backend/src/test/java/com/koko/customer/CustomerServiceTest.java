package com.koko.customer;

import com.koko.exception.DuplicateResourceException;
import com.koko.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

// This helps to open an close mocks without need for MockAnnotations
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    
    private CustomerService underTest;
    @Mock private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        AutoCloseable autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerService(customerDao);

    }

    @Test
    void canGetAllCustomers() {
        // When
        underTest.getAllCustomers();
        // Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomerById() {
        // Given
        int id = 1;
        int age = 60;
        Gender gender = ((age % 2) == 0) ? Gender.MALE : Gender.FEMALE;
        Customer customer = new Customer(id, "koko", "koko@example.com", age, gender);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        Customer actual = underTest.getCustomerById(id);
        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void WillThrowWhenCanGetCustomerByIdReturnsEmpty() {
        // Given
        int id = 1;
        // When
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        // Then
        assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with id [%s] not found!".formatted(id));
    }

    @Test
    void canAddCustomer() {
        // Given
        String email = "koko@example.com";
        when(customerDao.existsCustomerEmail(email)).thenReturn(false);
        int age = 19;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "koko",
                email,
                age,
                gender
        );
        // When
        underTest.addCustomer(request);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer actual = customerArgumentCaptor.getValue();
        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo(request.name());
        assertThat(actual.getEmail()).isEqualTo(request.email());
        assertThat(actual.getAge()).isEqualTo(request.age());

    }

    @Test
    void willThrowIfEmailExistsWhenAddingCustomer() {
        // Given
        String email = "koko@example.com";
        when(customerDao.existsCustomerEmail(email)).thenReturn(true);
        int age = 19;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        // When
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "koko",
                email,
                age,
                gender
        );
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");
        // Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void canDeleteCustomerById() {
        // Given
        int id = 1;
        when(customerDao.existsCustomerId(id)).thenReturn(true);
        // When
        underTest.deleteCustomerById(id);
        // Then
        verify(customerDao).removeCustomerById(id);
    }

    @Test
    void willThrowIfCustomerIdDoesNotExistWhenDeletingCustomerById() {
        // Given
        int id = 1;
        when(customerDao.existsCustomerId(id)).thenReturn(false);
        // When
        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer with [%s] not found!".formatted(id));
        ;
        // Then
        verify(customerDao, never()).removeCustomerById(id);
    }

    @Test
    void canUpdateAllCustomerProperties() {
        // Given
        int id = 1;
        int age = 19;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        Customer customer = new Customer(id, "koko", "koko@example.com", age, gender);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                "kokoaiye",
                "kokoaiye@example.com",
                56,
                gender
        );
        when(customerDao.existsCustomerEmail(updateRequest.email())).thenReturn(false);
        underTest.updateCustomer(id, updateRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void canUpdateCustomerName() {
        // Given
        int id = 1;
        int age = 19;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        Customer customer = new Customer(id, "koko", "koko@example.com", age, gender);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                "kokoaiye",
                null,
                null,
                gender
        );
        underTest.updateCustomer(id, updateRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateCustomerEmail() {
        // Given
        int id = 1;
        int age = 19;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        Customer customer = new Customer(id, "koko", "koko@example.com", age, gender);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                null,
                "kokoaiye@example.com",
                null,
                gender
        );
        when(customerDao.existsCustomerEmail(updateRequest.email())).thenReturn(false);
        underTest.updateCustomer(id, updateRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void canUpdateCustomerAge() {
        // Given
        int id = 1;
        int age = 19;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        Customer customer = new Customer(id, "koko", "koko@example.com", 23, gender);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                null,
                null,
                43,
                gender
        );
        underTest.updateCustomer(id, updateRequest);
        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isEqualTo(id);
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void willThrowIfEmailAlreadyExistsWhenUpdatingCustomer() {
        // Given
        int id = 1;
        int age = 19;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        Customer customer = new Customer(id, "koko", "koko@example.com", age, gender);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                null,
                "kokoaiye@example.com",
                null,
                gender
        );
        when(customerDao.existsCustomerEmail(updateRequest.email())).thenReturn(true);
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists!");
        // Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void willThrowIfNoPropertyIsGivenWhenUpdatingCustomer() {
        // Given
        int id = 1;
        int age = 19;
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        Customer customer = new Customer(id, "koko", "koko@example.com", age, gender);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        // When
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                null,
                null,
                null,
                gender
        );
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Try again, customer info has not changed");
        // Then
        verify(customerDao, never()).updateCustomer(any());
    }
}