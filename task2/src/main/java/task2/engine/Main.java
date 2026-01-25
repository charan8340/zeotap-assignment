package task2.engine;

import task2.engine.config.ConfigLoader;
import task2.engine.config.AppConfig;
import task2.engine.core.Orchestrator;

public class Main {

    public static void main(String[] args) {
        AppConfig config = ConfigLoader.load("app.properties");
        Orchestrator orchestrator = new Orchestrator(config);
        orchestrator.start();
    }
}
