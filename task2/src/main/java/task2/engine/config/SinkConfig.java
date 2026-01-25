package task2.engine.config;

import java.util.HashMap;
import java.util.Map;

public class SinkConfig {

    public boolean enabled;
    public int rateLimitPerSecond;
    public int queueCapacity;
    public int workers;
    public String format;

    // ✅ ADD THIS METHOD
    public static Map<String, SinkConfig> defaultSinks() {

        Map<String, SinkConfig> sinks = new HashMap<>();

        SinkConfig rest = new SinkConfig();
        rest.enabled = true;
        rest.rateLimitPerSecond = 50;
        rest.queueCapacity = 100;
        rest.workers = 1;
        rest.format = "json";
        sinks.put("rest", rest);

        SinkConfig db = new SinkConfig();
        db.enabled = true;
        db.rateLimitPerSecond = 100;
        db.queueCapacity = 100;
        db.workers = 2;
        db.format = "json";
        sinks.put("db", db);

        SinkConfig grpc = new SinkConfig();
        grpc.enabled = true;
        grpc.rateLimitPerSecond = 80;
        grpc.queueCapacity = 100;
        grpc.workers = 2;
        grpc.format = "json";
        sinks.put("grpc", grpc);

        SinkConfig mq = new SinkConfig();
        mq.enabled = true;
        mq.rateLimitPerSecond = 120;
        mq.queueCapacity = 100;
        mq.workers = 2;
        mq.format = "xml";
        sinks.put("mq", mq);

        return sinks;
    }
}
