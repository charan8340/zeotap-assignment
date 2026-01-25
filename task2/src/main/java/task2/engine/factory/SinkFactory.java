package task2.engine.factory;

import task2.engine.sink.*;

public interface SinkFactory {
    Sink create(SinkType type);
}
