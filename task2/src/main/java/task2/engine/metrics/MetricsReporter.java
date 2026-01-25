package task2.engine.metrics;

import task2.engine.sink.SinkType;

public class MetricsReporter implements Runnable {

    private final MetricsRegistry registry;
    private long last = 0;

    public MetricsReporter(MetricsRegistry registry) {
        this.registry = registry;
    }

    public void run() {
        long now = registry.total();
        long throughput = now - last;
        last = now;

        System.out.println("Processed: " + now + " | TPS: " + throughput);
        for (SinkType t : SinkType.values()) {
            System.out.println(
                t + " -> success=" + registry.success(t) +
                " failure=" + registry.failure(t)
            );
        }
        System.out.println("----");
    }
}
