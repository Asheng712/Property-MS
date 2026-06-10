package com.wisdom.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private static final String TEST_SECRET = "test-secret-key-for-jwt-token-generation-256-bit-minimum-length";
    private static final long TEST_EXPIRATION = 86400000L; // 24小时

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil(TEST_SECRET, TEST_EXPIRATION);
    }

    @Test
    void generateTokenShouldReturnNonNullToken() {
        String token = jwtTokenUtil.generateToken(1L, "admin");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void generateTokenShouldContainUserId() {
        String token = jwtTokenUtil.generateToken(1L, "admin");

        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        assertEquals(1L, userId);
    }

    @Test
    void generateTokenShouldContainUsername() {
        String token = jwtTokenUtil.generateToken(1L, "admin");

        String username = jwtTokenUtil.getUsernameFromToken(token);
        assertEquals("admin", username);
    }

    @Test
    void getUserIdFromTokenShouldReturnNullForInvalidToken() {
        Long userId = jwtTokenUtil.getUserIdFromToken("invalid.token.here");

        assertNull(userId);
    }

    @Test
    void getUserIdFromTokenShouldReturnNullForEmptyToken() {
        Long userId = jwtTokenUtil.getUserIdFromToken("");

        assertNull(userId);
    }

    @Test
    void getUserIdFromTokenShouldReturnNullForNullToken() {
        Long userId = jwtTokenUtil.getUserIdFromToken(null);

        assertNull(userId);
    }

    @Test
    void getUsernameFromTokenShouldReturnNullForInvalidToken() {
        String username = jwtTokenUtil.getUsernameFromToken("invalid.token.here");

        assertNull(username);
    }

    @Test
    void getUsernameFromTokenShouldReturnNullForEmptyToken() {
        String username = jwtTokenUtil.getUsernameFromToken("");

        assertNull(username);
    }

    @Test
    void validateTokenShouldReturnTrueForValidToken() {
        String token = jwtTokenUtil.generateToken(1L, "admin");

        assertTrue(jwtTokenUtil.validateToken(token));
    }

    @Test
    void validateTokenShouldReturnFalseForInvalidToken() {
        assertFalse(jwtTokenUtil.validateToken("invalid.token.here"));
    }

    @Test
    void validateTokenShouldReturnFalseForEmptyToken() {
        assertFalse(jwtTokenUtil.validateToken(""));
    }

    @Test
    void validateTokenShouldReturnFalseForNullToken() {
        assertFalse(jwtTokenUtil.validateToken(null));
    }

    @Test
    void validateTokenShouldReturnFalseForExpiredToken() {
        // Use a token util with 0 expiration (immediately expired)
        JwtTokenUtil expiredTokenUtil = new JwtTokenUtil(TEST_SECRET, 0L);
        String token = expiredTokenUtil.generateToken(1L, "admin");

        assertFalse(expiredTokenUtil.validateToken(token));
    }

    @Test
    void generateTokenWithDifferentUserIdsShouldReturnDifferentTokens() {
        String token1 = jwtTokenUtil.generateToken(1L, "admin");
        String token2 = jwtTokenUtil.generateToken(2L, "user");

        assertNotEquals(token1, token2);
        assertEquals(1L, jwtTokenUtil.getUserIdFromToken(token1));
        assertEquals(2L, jwtTokenUtil.getUserIdFromToken(token2));
    }

    @Test
    void getUsernameFromTokenShouldReturnNullForNullToken() {
        String username = jwtTokenUtil.getUsernameFromToken(null);

        assertNull(username);
    }

    @Test
    void validateTokenShouldReturnFalseForTamperedToken() {
        String token = jwtTokenUtil.generateToken(1L, "admin");
        String tamperedToken = token.substring(0, token.length() - 4) + "xxxx";

        assertFalse(jwtTokenUtil.validateToken(tamperedToken));
    }
}
