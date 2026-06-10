package com.wisdom.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MybatisPlusConfigTest {

    @Test
    void mybatisPlusInterceptorShouldReturnInterceptorWithPaginationInnerInterceptor() {
        MybatisPlusConfig config = new MybatisPlusConfig();
        MybatisPlusInterceptor interceptor = config.mybatisPlusInterceptor();

        assertNotNull(interceptor);
        assertNotNull(interceptor.getInterceptors());
    }

    @Test
    void mybatisPlusInterceptorShouldContainPaginationInnerInterceptor() {
        MybatisPlusConfig config = new MybatisPlusConfig();
        MybatisPlusInterceptor interceptor = config.mybatisPlusInterceptor();

        long paginationCount = interceptor.getInterceptors().stream()
                .filter(i -> i instanceof PaginationInnerInterceptor)
                .count();
        org.junit.jupiter.api.Assertions.assertEquals(1, paginationCount);
    }
}
