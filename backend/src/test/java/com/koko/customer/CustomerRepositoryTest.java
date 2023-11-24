package com.koko.customer;

import com.koko.AbstractTestContainers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void existsCustomerByEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(name, email, 45, Gender.MALE);

        underTest.save(customer);
        // When
        boolean actual = underTest.existsCustomerByEmail(email);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailReturnsFalseWhenEmailNotPresent() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // When
        boolean actual = underTest.existsCustomerByEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = FAKER.name().fullName();
        Customer customer = new Customer(name, email, 45, Gender.MALE);

        underTest.save(customer);
        // When
        int id = underTest
                .findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        boolean actual = underTest.existsCustomerById(id);
        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdReturnsFalseWhenIdNotPresent() {
        // Given
        int id = -1;
        // When
        boolean actual = underTest.existsCustomerById(id);
        // Then
        assertThat(actual).isFalse();
    }
}