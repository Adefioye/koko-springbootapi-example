package com.koko.auth;

public record AuthRequest(
        String username,
        String password
) {
}
