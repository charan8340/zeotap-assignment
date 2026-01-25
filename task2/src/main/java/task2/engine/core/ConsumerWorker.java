package task2.engine.core;

import task2.engine.metrics.*;
import task2.engine.retry.RetryPolicy;

import task2.engine.sink.*;
import task2.engine.throttling.ThrottlePolicy;
import task2.engine.transform.Transformer;

import java.util.concurrent.BlockingQueue;

public class ConsumerWorker implements Runnable {

    private final BlockingQueue<String> queue;
    private final Transformer transformer;
    private final RetryPolicy retry;

    private final Sink sink;
    private final ThrottlePolicy throttle;
    private final MetricsRegistry metrics;

    public ConsumerWorker(
        BlockingQueue<String> queue,
        Sink sink,
        Transformer transformer,
        RetryPolicy retry,
        ThrottlePolicy throttle,
        MetricsRegistry metrics
    ) {
        this.queue = queue;
        this.sink = sink;
        this.transformer = transformer;
        this.retry = retry;
        this.throttle = throttle;
        this.metrics = metrics;
    }

    public void run() {
        while (true) {
//            System.out.println("Consumer Started");
            try {
                String record = queue.take();
                byte[] payload = transformer.transform(record);
                int attempt = 0;

                while (true) {
                    try {
                        throttle.acquire();
                        sink.send(payload);
                        metrics.recordSuccess(sink.type());
                        break;
                    } catch (Exception e) {
                        attempt++;
                        if (!retry.allow(attempt)) {
                            metrics.recordFailure(sink.type());
                            break;
                        }
                    }
                }
            } catch (Exception ignored) {}
        }
    }
}
