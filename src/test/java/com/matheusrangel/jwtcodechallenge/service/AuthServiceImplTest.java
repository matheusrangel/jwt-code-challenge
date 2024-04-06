package com.matheusrangel.jwtcodechallenge.service;

import com.matheusrangel.jwtcodechallenge.config.JWTProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    @Mock
    private JWTProperties jwtProperties;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Caso 1 - Token JWT valido")
    void authenticate_withValidUnsignedToken_shouldReturnTrue() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";
        when(jwtProperties.getSigned()).thenReturn(false);
        when(jwtProperties.getValidRoles()).thenReturn(Set.of("Member", "Admin"));

        // Act
        Boolean result = authService.authenticate(token);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Caso 2 - Token JWT invalido")
    void authenticate_withInvalidUnsignedToken_shouldReturnFalse() {
        // Arrange
        String token = "invalid.token";
        when(jwtProperties.getSigned()).thenReturn(false);
        when(jwtProperties.getValidRoles()).thenReturn(Set.of("Member", "Admin"));

        // Act
        Boolean result = authService.authenticate(token);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Caso 3 - Claim Inválida")
    void authenticate_withInvalidClaim_shouldReturnFalse() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";
        when(jwtProperties.getSigned()).thenReturn(false);
        when(jwtProperties.getValidRoles()).thenReturn(Set.of("Member", "Admin", "External"));

        // Act
        Boolean result = authService.authenticate(token);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Caso 4 - Mais de 3 Claims")
    void authenticate_withMoreThan3Claims_shouldReturnFalse() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiTWVtYmVyIiwiT3JnIjoiQlIiLCJTZWVkIjoiMTQ2MjciLCJOYW1lIjoiVmFsZGlyIEFyYW5oYSJ9.cmrXV_Flm5mfdpfNUVopY_I2zeJUy4EZ4i3Fea98zvY";
        when(jwtProperties.getSigned()).thenReturn(false);
        when(jwtProperties.getValidRoles()).thenReturn(Set.of("Member", "Admin", "External"));

        // Act
        Boolean result = authService.authenticate(token);

        // Assert
        assertFalse(result);
    }

    @Test
    void authenticate_withNotPrimeSeedClaimValue_shouldReturnFalse() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiMTQ2IiwiTmFtZSI6Ik1hcmlhIE9saXZpYSJ9.hQruJCR3sJCCZ2QYWt-PAZqmlM3FYNy9GYxDS5TmKgo";
        when(jwtProperties.getSigned()).thenReturn(false);
        when(jwtProperties.getValidRoles()).thenReturn(Set.of("Member", "Admin", "External"));

        // Act
        Boolean result = authService.authenticate(token);

        // Assert
        assertFalse(result);
    }

    @Test
    void authenticate_withValidSignedToken_shouldReturnTrue() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyBTYW50b3MifQ.CG3D6eufhkJoD0sB6QyKXg1hwKARpTzXunSBYnkiRrs";
        when(jwtProperties.getSigned()).thenReturn(true);
        when(jwtProperties.getSigningKey()).thenReturn("24C826EFA558F4AEBAA36DBC8C2559276B11D64C9CDCC72C98BB3EE4C8");
        when(jwtProperties.getValidRoles()).thenReturn(Set.of("Member", "Admin"));

        // Act
        Boolean result = authService.authenticate(token);

        // Assert
        assertTrue(result);
    }

    @Test
    void authenticate_withInvalidSignedToken_shouldReturnFalse() {
        // Arrange
        String token = "invalid.token";
        when(jwtProperties.getSigned()).thenReturn(true);
        when(jwtProperties.getSigningKey()).thenReturn("secretKey");

        // Act
        Boolean result = authService.authenticate(token);

        // Assert
        assertFalse(result);
    }

    @Test
    void authenticate_withInvalidRoleClaim_shouldReturnFalse() {
        // Arrange
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiVGVzdGUiLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.LyN9v63oS8MCISvbR9MCaijcjzAwx8Om2aTnlWMaVYg";
        when(jwtProperties.getSigned()).thenReturn(false);
        when(jwtProperties.getValidRoles()).thenReturn(Set.of("Member", "Admin", "External"));

        // Act
        Boolean result = authService.authenticate(token);

        // Assert
        assertFalse(result);
    }
}