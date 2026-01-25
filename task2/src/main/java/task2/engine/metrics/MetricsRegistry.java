package task2.engine.metrics;

import task2.engine.sink.SinkType;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class MetricsRegistry {

    private final LongAdder total = new LongAdder();
    private final ConcurrentHashMap<SinkType, LongAdder> success = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<SinkType, LongAdder> failure = new ConcurrentHashMap<>();

    public void recordSuccess(SinkType t) {
        total.increment();
        success.computeIfAbsent(t, k -> new LongAdder()).increment();
    }

    public void recordFailure(SinkType t) {
        total.increment();
        failure.computeIfAbsent(t, k -> new LongAdder()).increment();
    }

    public long total() { return total.sum(); }
    public long success(SinkType t) { return success.getOrDefault(t, new LongAdder()).sum(); }
    public long failure(SinkType t) { return failure.getOrDefault(t, new LongAdder()).sum(); }
}
