package com.wisdom.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SecurityConfigTest {

    @Test
    void passwordEncoderShouldReturnNonNullEncoder() {
        SecurityConfig config = new SecurityConfig();
        BCryptPasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
    }

    @Test
    void passwordEncoderShouldCorrectlyEncodeAndMatch() {
        SecurityConfig config = new SecurityConfig();
        BCryptPasswordEncoder encoder = config.passwordEncoder();

        String rawPassword = "securePassword123";
        String encoded = encoder.encode(rawPassword);

        assertNotNull(encoded);
        org.junit.jupiter.api.Assertions.assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    void corsFilterShouldReturnNonNullCorsFilter() {
        SecurityConfig config = new SecurityConfig();
        org.springframework.web.filter.CorsFilter corsFilter = config.corsFilter();

        assertNotNull(corsFilter);
    }

    @Test
    void corsFilterShouldConfigureCorrectCorsSettings() {
        SecurityConfig config = new SecurityConfig();
        org.springframework.web.filter.CorsFilter corsFilter = config.corsFilter();

        UrlBasedCorsConfigurationSource source = (UrlBasedCorsConfigurationSource)
                org.springframework.test.util.ReflectionTestUtils.getField(corsFilter, "configSource");
        assertNotNull(source);

        CorsConfiguration corsConfig = source.getCorsConfigurations().get("/api/**");
        assertNotNull(corsConfig);
        assertEquals(1, corsConfig.getAllowedOriginPatterns().size());
        assertEquals("*", corsConfig.getAllowedOriginPatterns().iterator().next());
        assertEquals(5, corsConfig.getAllowedMethods().size());
        org.junit.jupiter.api.Assertions.assertTrue(
                corsConfig.getAllowedHeaders().contains("Authorization"));
    }
}
