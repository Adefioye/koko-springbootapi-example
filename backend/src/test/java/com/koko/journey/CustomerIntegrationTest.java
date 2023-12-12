package com.koko.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.koko.customer.CustomerDTO;
import com.koko.customer.CustomerRegistrationRequest;
import com.koko.customer.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
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
        String password = "password";

        CustomerRegistrationRequest customerRequest = new CustomerRegistrationRequest(
             name, email, password, age, gender
        );
        // Send a POST request
        String jwtToken = webTestClient.post()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // Get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // Get customer by id
        assert allCustomers != null;
        Integer id = allCustomers
                .stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email
        );
        assertThat(allCustomers).contains(expectedCustomer);


        webTestClient.get()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDeleteCustomer() {
        Faker FAKER = new Faker();
        Name fakerName = FAKER.name();
        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@foofooobar.com";
        String password = "password";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest customerRequest = new CustomerRegistrationRequest(
                name, email, password, age, gender
        );
        // Send a POST request
        String jwtToken = webTestClient.post()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // Get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // Get customer by id
        Integer id = Objects.requireNonNull(allCustomers)
                .stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email
        );

        assertThat(allCustomers).contains(expectedCustomer);
        System.out.println("Asserted expected customer");

        webTestClient.delete()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectStatus()
                .isOk();

        webTestClient.get()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
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
        String password = "password";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest customerRequest = new CustomerRegistrationRequest(
                name, email, password, age, gender
        );
        // Send a POST request
        String jwtToken = webTestClient.post()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        // Get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOM_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        // Get customer by id
        Integer id = Objects.requireNonNull(allCustomers)
                .stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                name,
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email
        );
        assertThat(allCustomers).contains(expectedCustomer);


        // Make update to the customer with the id
        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                "koko", null, "newPassword", null, gender
        );

        // Send a PUT call to make update
        webTestClient.put()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .body(Mono.just(updateRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // Retrieve update result
        CustomerDTO actualUpdatedCustomer = webTestClient.get()
                .uri(CUSTOM_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .exchange()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        CustomerDTO expectedUpdatedCustomer = new CustomerDTO(
                id,
                updateRequest.name(),
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email
        );

        assertThat(actualUpdatedCustomer).isEqualTo(expectedUpdatedCustomer);
    }
}
