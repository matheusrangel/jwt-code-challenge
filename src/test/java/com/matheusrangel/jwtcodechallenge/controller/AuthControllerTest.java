package com.matheusrangel.jwtcodechallenge.controller;

import com.matheusrangel.jwtcodechallenge.dto.response.AuthResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Integration - CASE 1 - Valid Token")
    public void testAuthenticateWithValidToken() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg");
        var request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(
                "/v1/auth", HttpMethod.GET, request, AuthResponse.class);

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().valid());
    }

    @Test
    @DisplayName("Integration - CASE 2 - Invalid Token")
    public void testAuthenticateWithInvalidToken() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "invalid_token");
        var request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(
                "/v1/auth", HttpMethod.GET, request, AuthResponse.class);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().valid());
    }

    @Test
    @DisplayName("Integration - CASE 3 - Invalid Claim")
    public void testAuthenticateWithInvalidClaim() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs");
        var request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(
                "/v1/auth", HttpMethod.GET, request, AuthResponse.class);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().valid());
    }

    @Test
    @DisplayName("Integration - CASE 4 - More than 3 Claims")
    public void testAuthenticateWithMoreThan3Claims() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY");
        var request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(
                "/v1/auth", HttpMethod.GET, request, AuthResponse.class);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().valid());
    }

    @Test
    @DisplayName("Integration - Not prime seed")
    public void testAuthenticateWithNotPrimeSeed() {
        var headers = new HttpHeaders();
        headers.set("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiMTQ2IiwiTmFtZSI6Ik1hcmlhIE9saXZpYSJ9.hQruJCR3sJCCZ2QYWt-PAZqmlM3FYNy9GYxDS5TmKgo");
        var request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(
                "/v1/auth", HttpMethod.GET, request, AuthResponse.class);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().valid());
    }
}