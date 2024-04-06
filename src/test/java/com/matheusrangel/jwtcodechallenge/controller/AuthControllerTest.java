package com.matheusrangel.jwtcodechallenge.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAuthenticateWithValidToken() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg");
        var request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(
                "/v1/auth", HttpMethod.GET, request, Boolean.class);

        assertEquals(OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }

    @Test
    public void testAuthenticateWithInvalidToken() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "invalid_token");
        var request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(
                "/v1/auth", HttpMethod.GET, request, Boolean.class);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, response.getBody());
    }
}