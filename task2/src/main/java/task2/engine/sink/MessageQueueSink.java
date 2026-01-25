package task2.engine.sink;

public class MessageQueueSink implements Sink {

    @Override
    public SinkType type() {
        return SinkType.MQ;
    }

    @Override
    public void send(byte[] payload) throws Exception {
        // simulate publish latency
        Thread.sleep(5);

        if (Math.random() < 0.01) {
            throw new RuntimeException("MQ publish failed");
        }
    }
}
