package task2.engine.core;

import task2.engine.config.*;
import task2.engine.factory.*;
import task2.engine.metrics.*;
import task2.engine.retry.*;
import task2.engine.sink.*;
import task2.engine.throttling.*;
import task2.engine.transform.*;

import java.util.*;
import java.util.concurrent.*;

public class Orchestrator {

    private final AppConfig config;

    public Orchestrator(AppConfig config) {
        this.config = config;
    }

    public void start() {

        SinkFactory sinkFactory = new DefaultSinkFactory();
        MetricsRegistry metrics = new MetricsRegistry();
        RetryPolicy retry = new RetryPolicy(config.maxRetries);

        // new thread for each sink
        ExecutorService consumerExecutor =
                Executors.newFixedThreadPool(
                        Runtime.getRuntime().availableProcessors()
                );

        List<BlockingQueue<String>> allQueues = new ArrayList<>();

        // 1️⃣ Setup sinks and submit CONSUMERS
        for (var entry : config.sinks.entrySet()) {

            SinkConfig sc = entry.getValue();
            if (!sc.enabled) continue;

            SinkType type = SinkType.valueOf(entry.getKey().toUpperCase());
            Sink sink = sinkFactory.create(type);

            BlockingQueue<String> queue =
                    new ArrayBlockingQueue<>(sc.queueCapacity);
            allQueues.add(queue);

            Transformer transformer = switch (sc.format) {
                case "json" -> new JsonTransformer();
                case "xml" -> new XmlTransformer();
                default -> new MapTransformer();
            };

            ThrottlePolicy throttle =
                    new ThrottlePolicy(sc.rateLimitPerSecond);

            for (int i = 0; i < sc.workers; i++) {
                consumerExecutor.submit(
                        new ConsumerWorker(
                                queue,
                                sink,
                                transformer,
                                retry,
                                throttle,
                                metrics
                        )
                );
            }
        }

        // single thread for producer
        Thread producerThread =
                new Thread(new Producer(allQueues));//, config.inputFile));
        producerThread.setName("producer-thread");
        producerThread.start();

        // 3️⃣ Metrics reporter (simple, non-blocking)
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        new MetricsReporter(metrics).run();
                    }
                },
                config.metricsIntervalSeconds * 1000L,
                config.metricsIntervalSeconds * 1000L
        );
    }
}
