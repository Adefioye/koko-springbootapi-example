package com.koko.auth;

import com.koko.customer.CustomerDTO;

public record AuthResponse(
        String token,
        CustomerDTO customerDTO
) {
}
