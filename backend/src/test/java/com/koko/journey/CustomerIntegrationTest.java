package com.koko.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.koko.customer.Customer;
import com.koko.customer.CustomerRegistrationRequest;
import com.koko.customer.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String CUSTOM_URI = "/api/v1/customers";

    @Test
    void canRegisterACustomer() {
        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@foofooobar.com";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest customerRequest = new CustomerRegistrationRequest(
             name, email, age, gender
        );
        // Send a POST request
        webTestClient.post()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(name, email, "password", gender, age);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // Get customer by id
        Integer id = Objects.requireNonNull(allCustomers)
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);

        webTestClient.get()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDeleteCustomer() {
        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@foofooobar.com";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest customerRequest = new CustomerRegistrationRequest(
                name, email, age, gender
        );
        // Send a POST request
        webTestClient.post()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer(name, email, "password", gender, age);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // Get customer by id
        Integer id = Objects.requireNonNull(allCustomers)
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        webTestClient.delete()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@foofooobar.com";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest customerRequest = new CustomerRegistrationRequest(
                name, email, age, gender
        );
        // Send a POST request
        webTestClient.post()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        Customer expectedCustomerRequest = new Customer(name, email, "password", gender, age);
        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomerRequest);

        // Get customer by id
        Integer id = Objects.requireNonNull(allCustomers)
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // Make update to the customer with the id
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                "koko", null, null, gender
        );

        // Send a PUT call to make update
        webTestClient.put()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Retrieve update result
        Customer actualUpdatedCustomer = webTestClient.get()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        Customer expectedUpdatedCustomer = new Customer(
                id, updateRequest.name(), email, "password", gender, age);

        assertThat(actualUpdatedCustomer).isEqualTo(expectedUpdatedCustomer);
    }
}
