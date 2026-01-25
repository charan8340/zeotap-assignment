package task2.engine.sink;

public class GrpcSink implements Sink {

    @Override
    public SinkType type() {
        return SinkType.GRPC;
    }

    @Override
    public void send(byte[] payload) throws Exception {
        // simulate gRPC call
        Thread.sleep(15);

        if (Math.random() < 0.03) {
            throw new RuntimeException("gRPC call failed");
        }
    }
}
