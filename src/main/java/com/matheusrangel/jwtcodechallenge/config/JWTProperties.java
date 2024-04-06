package com.matheusrangel.jwtcodechallenge.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "jwt")
@Getter
@RequiredArgsConstructor
public class JWTProperties {

    private final Boolean signed;
    private final String signingKey;
    private final Set<String> validRoles;
}
