package com.koko.customer;

public record CustomerRegistrationRequest (
        String name,
        String email,
        Integer age
) {
}
