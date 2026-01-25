package task2.engine.sink;

public class RestSink implements Sink {

    @Override
    public SinkType type() {
        return SinkType.REST;
    }

    @Override
    public void send(byte[] payload) throws Exception {
        // simulate HTTP latency
        Thread.sleep(20);

        if (Math.random() < 0.05) {
            throw new RuntimeException("HTTP 500");
        }
    }
}
