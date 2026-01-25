package task2.engine.retry;

public class RetryPolicy {
    private final int max;

    public RetryPolicy(int max) {
        this.max = max;
    }

    public boolean allow(int attempt) {
        return attempt < max;
    }
}
