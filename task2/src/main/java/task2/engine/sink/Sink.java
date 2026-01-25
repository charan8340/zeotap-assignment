package task2.engine.sink;

public interface Sink {
    SinkType type();
    void send(byte[] payload) throws Exception;
}
