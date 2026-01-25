package task2.engine.throttling;

import com.google.common.util.concurrent.RateLimiter;

public class ThrottlePolicy {
    private final RateLimiter limiter;

    public ThrottlePolicy(int rate) {
        this.limiter = RateLimiter.create(rate);
    }

    public void acquire() {
        limiter.acquire();
    }
}
