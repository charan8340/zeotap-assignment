package task2.engine.sink;

public class DatabaseSink implements Sink {

    @Override
    public SinkType type() {
        return SinkType.DB;
    }

    @Override
    public void send(byte[] payload) throws Exception {
        // simulate DB latency
        Thread.sleep(10);

        // simulate transient failure
        if (Math.random() < 0.02) {
            throw new RuntimeException("DB write failed");
        }
    }
}
