package com.koko.customer;

import com.koko.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        // Setup jdbctemplate with the datasource info
        // Set up customer rowmapper
        underTest = new CustomerJDBCDataAccessService(
            getJdbcTemplate(), customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {

        // Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                "password", Gender.MALE, 20
        );
        underTest.insertCustomer(customer);
        // When
        List<Customer> actual = underTest.selectAllCustomers();
        // Then
        assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", Gender.MALE, 20
        );
        underTest.insertCustomer(customer);
        // When
        int id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        Optional<Customer> actual = underTest.selectCustomerById(id);
        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willBeEmptyWhenSelectCustomerById() {
        // Given
        int id = -1;
        // When
        Optional<Customer> actual = underTest.selectCustomerById(id);
        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void removeCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", Gender.MALE, 48
        );
        underTest.insertCustomer(customer);
        // When
        int id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        underTest.removeCustomerById(id);
        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void updateCustomerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", Gender.MALE, 67
        );
        underTest.insertCustomer(customer);
        // When
        int id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        String newName = "koko";
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);
        underTest.updateCustomer(update);

        // Then
        String actual = underTest
                .selectCustomerById(id)
                .stream()
                .map(Customer::getName)
                .findFirst()
                .orElseThrow();
        assertThat(actual).isEqualTo(newName);

    }

    @Test
    void UpdateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", Gender.MALE, 29
        );
        underTest.insertCustomer(customer);
        // When
        int id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        String newEmail = "koko@example.com";
        customer.setId(id);
        customer.setEmail(newEmail);
        underTest.updateCustomer(customer);
        // Then
        String actual = underTest
                .selectCustomerById(id)
                .stream()
                .map(Customer::getEmail)
                .findFirst()
                .orElseThrow();
        assertThat(actual).isEqualTo(newEmail);
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", Gender.MALE, 67
        );
        underTest.insertCustomer(customer);
        // When
        int id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        int newAge = 45;
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);

        // Then
        int actual = underTest
                .selectCustomerById(id)
                .stream()
                .map(Customer::getAge)
                .findFirst()
                .orElseThrow();
        assertThat(actual).isEqualTo(newAge);
    }

    @Test
    void willNotUpdateCustomerWhenNothingToUpdate() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", Gender.MALE, 67
        );
        underTest.insertCustomer(customer);
        // When
        int id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        Customer update = new Customer();
        update.setId(id);
        underTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void insertCustomer() {

        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", Gender.MALE, 67
        );
        // When
        underTest.insertCustomer(customer);
        int id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });


    }

    @Test
    void existsCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(name, email, "password", Gender.MALE, 45);

        underTest.insertCustomer(customer);
        // When
        boolean actual = underTest.existsCustomerEmail(email);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerEmailReturnsFalseWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // When
        boolean actual = underTest.existsCustomerEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerId() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
               FAKER.name().fullName(),
               email,
                "password", Gender.MALE, 34
        );
        underTest.insertCustomer(customer);
        // When
        Integer id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        boolean actual = underTest.existsCustomerId(id);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerIdReturnsFalseWhenIdNotPresent() {
        // Given
        int id = -1;
        // When
        boolean actual = underTest.existsCustomerId(id);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    public void updateAllCustomerProperties() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password", Gender.MALE, 20
        );
        underTest.insertCustomer(customer);

        Integer id = underTest
                .selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When update name, email, age
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        String newEmail = UUID.randomUUID().toString();
        update.setEmail(newEmail);
        update.setAge(22);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(updated -> {
                assertThat(updated.getId()).isEqualTo(id);
                assertThat(updated.getGender()).isEqualTo(Gender.MALE);
                assertThat(updated.getName()).isEqualTo("foo");
                assertThat(updated.getEmail()).isEqualTo(newEmail);
                assertThat(updated.getAge()).isEqualTo(22);
    });
    }
}