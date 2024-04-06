package com.matheusrangel.jwtcodechallenge.service;

import com.matheusrangel.jwtcodechallenge.config.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private static final String CLAIM_NAME = "Name";
    private static final String CLAIM_ROLE = "Role";
    private static final String CLAIM_SEED = "Seed";

    private final JWTProperties jwtProperties;

    public AuthServiceImpl(JWTProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Boolean authenticate(String token) {
        try {
            var claims = parseToken(token);
            return validateClaims(claims);
        } catch (Exception ex) {
            log.error("Invalid JWT: " + ex.getMessage());
        }

        return false;
    }

    private Claims parseToken(String token) {
        if (jwtProperties.getSigned()) {
            return parseSignedJwt(token);
        }

        return parseUnsignedJwt(token);
    }

    private Claims parseUnsignedJwt(String token) {
        String[] splitToken = token.split("\\.");
        String unsignedToken = splitToken[0] + "." + splitToken[1] + ".";

        return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(unsignedToken)
                .getBody();
    }

    private Claims parseSignedJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtProperties.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateClaims(Claims claims) {
        return hasRequiredClaims(claims)
                && validateNameClaim(claims)
                && validateRoleClaim(claims)
                && validateSeedClaim(claims);
    }

    private boolean hasRequiredClaims(Claims claims) {
        boolean hasAllClaims = claims.size() == 3
                && claims.containsKey(CLAIM_NAME)
                && claims.containsKey(CLAIM_ROLE)
                && claims.containsKey(CLAIM_SEED);
        if (!hasAllClaims) {
            log.error("Invalid JWT: Incorrect claims");
        }

        return hasAllClaims;
    }

    private boolean validateNameClaim(Claims claims) {
        String name = claims.get(CLAIM_NAME, String.class);
        boolean isValidName = !(name.matches(".*\\d.*") || name.length() > 256);
        if (!isValidName) {
            log.error("Invalid JWT: Invalid Name claim");
        }

        return isValidName;
    }

    private boolean validateRoleClaim(Claims claims) {
        String role = claims.get(CLAIM_ROLE, String.class);
        boolean isValidRole = jwtProperties.getValidRoles().contains(role);
        if (!isValidRole) {
            log.error("Invalid JWT: Invalid Role claim");
        }

        return isValidRole;
    }

    private boolean validateSeedClaim(Claims claims) {
        var seedClaim = claims.get(CLAIM_SEED, String.class);
        var seed = Integer.parseInt(seedClaim);

        boolean isPrimeSeed = isPrime(seed);
        if (!isPrimeSeed) {
            log.error("Invalid JWT: Seed is not prime");
        }

        return isPrimeSeed;
    }

    private boolean isPrime(int seed) {
        for (int i = 2; i < seed; i++) {
            if (seed % i == 0) {
                return false;
            }
        }

        return true;
    }
}
