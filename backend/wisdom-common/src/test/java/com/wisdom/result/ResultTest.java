package com.wisdom.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResultTest {

    @Test
    void successWrapsData() {
        Result<String> result = Result.success("ok");

        assertEquals(200, result.getCode());
        assertEquals("success", result.getMsg());
        assertEquals("ok", result.getData());
    }

    @Test
    void errorWrapsMessage() {
        Result<String> result = Result.error("failed");

        assertEquals(500, result.getCode());
        assertEquals("failed", result.getMsg());
        assertNull(result.getData());
    }
}
