package com.imago.backend.config;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class JwtConfig {
    private final Dotenv dotenv;

    public JwtConfig(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    public String getSecret() {
        return dotenv.get("JWT_SECRET", "fallbackSecretForDevelopment123!");
    }

    public long getExpiration() {
        return Long.parseLong(dotenv.get("JWT_EXPIRATION", "86400000"));
    }
}
