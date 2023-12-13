package com.koko.journey;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.koko.auth.AuthRequest;
import com.koko.auth.AuthResponse;
import com.koko.customer.CustomerDTO;
import com.koko.customer.CustomerRegistrationRequest;
import com.koko.customer.Gender;
import com.koko.jwt.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JWTUtil jwtUtil;

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";
    private static final String AUTH_URI = "/api/v1/auth";

    @Test
    void canAuthenticateAUser() {

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
        AuthRequest authRequest = new AuthRequest(
                email, password
        );

        // Authenticating with non-registered customer
        webTestClient.post()
                .uri(AUTH_URI + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        // Register a customer
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // Send auth request with (username and password)
        EntityExchangeResult<AuthResponse> result = webTestClient.post()
                .uri(AUTH_URI + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(new ParameterizedTypeReference<AuthResponse>() {
                })
                .returnResult();

        String jwtToken = result.getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);
        CustomerDTO customer = Objects.requireNonNull(result.getResponseBody()).customerDTO();
        assertThat(jwtUtil.isTokenValid(jwtToken, email)).isTrue();

        assertThat(customer.email()).isEqualTo(email);
        assertThat(customer.age()).isEqualTo(age);
        assertThat(customer.gender()).isEqualTo(gender);
        assertThat(customer.username()).isEqualTo(email);
        assertThat(customer.roles()).isEqualTo(List.of("ROLE_USER"));
        assertThat(customer.name()).isEqualTo(name);




    }
}
