package task2.engine.factory;

import task2.engine.sink.*;

public class DefaultSinkFactory implements SinkFactory {

    public Sink create(SinkType type) {
        return switch (type) {
            case REST -> new RestSink();
            case GRPC -> new GrpcSink();
            case MQ -> new MessageQueueSink();
            case DB -> new DatabaseSink();
        };
    }
}
