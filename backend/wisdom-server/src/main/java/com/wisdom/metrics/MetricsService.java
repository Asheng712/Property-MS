package com.wisdom.metrics;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class MetricsService {

    private final AtomicLong totalRequests = new AtomicLong(0);
    private final AtomicLong totalErrors = new AtomicLong(0);
    private final AtomicLong totalResponseTimeMs = new AtomicLong(0);
    private final long startTime = System.currentTimeMillis();

    public void recordRequest(long responseTimeMs, boolean isError) {
        totalRequests.incrementAndGet();
        totalResponseTimeMs.addAndGet(responseTimeMs);
        if (isError) {
            totalErrors.incrementAndGet();
        }
    }

    public long getTotalRequests() {
        return totalRequests.get();
    }

    public long getTotalErrors() {
        return totalErrors.get();
    }

    public double getErrorRate() {
        long total = totalRequests.get();
        return total == 0 ? 0.0 : (double) totalErrors.get() / total * 100;
    }

    public double getAverageResponseTime() {
        long total = totalRequests.get();
        return total == 0 ? 0.0 : (double) totalResponseTimeMs.get() / total;
    }

    public long getUptimeSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }
}
