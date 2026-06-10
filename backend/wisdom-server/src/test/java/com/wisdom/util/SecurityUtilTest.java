package com.wisdom.util;

import com.wisdom.context.BaseContext;
import com.wisdom.exception.BusinessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecurityUtilTest {

    @BeforeEach
    void setUp() {
        BaseContext.setCurrentId(1L);
    }

    @AfterEach
    void tearDown() {
        BaseContext.removeCurrentId();
    }

    @Test
    void getCurrentUserIdShouldReturnCurrentUserId() {
        Long userId = SecurityUtil.getCurrentUserId();

        assertEquals(1L, userId);
    }

    @Test
    void getCurrentUserIdShouldReturnNullWhenNotSet() {
        BaseContext.removeCurrentId();

        Long userId = SecurityUtil.getCurrentUserId();

        assertNull(userId);
    }

    @Test
    void checkOwnershipShouldNotThrowWhenUserIsOwner() {
        assertDoesNotThrow(() -> SecurityUtil.checkOwnership(1L));
    }

    @Test
    void checkOwnershipShouldThrowUnauthorizedWhenUserIdIsNull() {
        BaseContext.removeCurrentId();

        BusinessException ex = assertThrows(BusinessException.class,
                () -> SecurityUtil.checkOwnership(1L));

        assertEquals("UNAUTHORIZED", ex.getMessage());
        assertEquals(401, ex.getCode());
    }

    @Test
    void checkOwnershipShouldThrowForbiddenWhenUserIsNotOwner() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> SecurityUtil.checkOwnership(2L));

        assertEquals("FORBIDDEN", ex.getMessage());
        assertEquals(403, ex.getCode());
    }

    @Test
    void isOwnerShouldReturnTrueWhenUserIsOwner() {
        assertTrue(SecurityUtil.isOwner(1L));
    }

    @Test
    void isOwnerShouldReturnFalseWhenUserIsNotOwner() {
        assertFalse(SecurityUtil.isOwner(2L));
    }

    @Test
    void isOwnerShouldReturnFalseWhenUserIdIsNull() {
        BaseContext.removeCurrentId();

        assertFalse(SecurityUtil.isOwner(2L));
    }
}
