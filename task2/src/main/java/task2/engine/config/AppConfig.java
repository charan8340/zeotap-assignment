package task2.engine.config;

import java.util.Map;

public class AppConfig {

    public String inputFile;
    public int metricsIntervalSeconds;
    public int maxRetries;

    public Map<String, SinkConfig> sinks;
}
