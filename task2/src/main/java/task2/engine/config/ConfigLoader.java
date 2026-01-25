package task2.engine.config;

import java.io.InputStream;
import java.util.*;

public class ConfigLoader {

    public static AppConfig load(String path) {

        try (InputStream in =
                     ConfigLoader.class.getClassLoader().getResourceAsStream(path)) {

            if (in == null) {
                throw new RuntimeException("Config file not found: " + path);
            }

            Properties props = new Properties();
            props.load(in);

            AppConfig config = new AppConfig();

            // global config
            config.inputFile = props.getProperty("inputFile");
            config.metricsIntervalSeconds =
                    Integer.parseInt(props.getProperty("metricsIntervalSeconds"));
            config.maxRetries =
                    Integer.parseInt(props.getProperty("maxRetries"));

            // sink configs
            Map<String, SinkConfig> sinkConfigs = new HashMap<>();

            String sinksCsv = props.getProperty("sinks");
            if (sinksCsv == null || sinksCsv.isBlank()) {
                throw new RuntimeException("No sinks defined in config");
            }

            for (String sinkName : sinksCsv.split(",")) {
                sinkName = sinkName.trim();

                SinkConfig sc = new SinkConfig();
                sc.enabled =
                        Boolean.parseBoolean(props.getProperty("sink." + sinkName + ".enabled"));
                sc.rateLimitPerSecond =
                        Integer.parseInt(props.getProperty("sink." + sinkName + ".rateLimitPerSecond"));
                sc.queueCapacity =
                        Integer.parseInt(props.getProperty("sink." + sinkName + ".queueCapacity"));
                sc.workers =
                        Integer.parseInt(props.getProperty("sink." + sinkName + ".workers"));
                sc.format =
                        props.getProperty("sink." + sinkName + ".format");

                sinkConfigs.put(sinkName, sc);
            }

            config.sinks = sinkConfigs;
            return config;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
}
