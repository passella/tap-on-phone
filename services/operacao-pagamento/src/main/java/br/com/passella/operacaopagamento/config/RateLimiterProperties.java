package br.com.passella.operacaopagamento.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimiterProperties {
    private long limitRefreshPeriod;
    private int limitForPeriod;
    private long timeoutDuration;
    private int maxConcurrentCalls;


    public long getLimitRefreshPeriod() {
        return limitRefreshPeriod;
    }

    public int getLimitForPeriod() {
        return limitForPeriod;
    }

    public long getTimeoutDuration() {
        return timeoutDuration;
    }

    public int getMaxConcurrentCalls() {
        return maxConcurrentCalls;
    }

    public void setLimitRefreshPeriod(final long limitRefreshPeriod) {
        this.limitRefreshPeriod = limitRefreshPeriod;
    }

    public void setLimitForPeriod(final int limitForPeriod) {
        this.limitForPeriod = limitForPeriod;
    }

    public void setTimeoutDuration(final long timeoutDuration) {
        this.timeoutDuration = timeoutDuration;
    }

    public void setMaxConcurrentCalls(final int maxConcurrentCalls) {
        this.maxConcurrentCalls = maxConcurrentCalls;
    }
}
