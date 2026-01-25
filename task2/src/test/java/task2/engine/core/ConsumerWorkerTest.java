package task2.engine.core;

import org.junit.jupiter.api.Test;
import task2.engine.metrics.MetricsRegistry;
import task2.engine.retry.RetryPolicy;
import task2.engine.sink.*;
import task2.engine.throttling.ThrottlePolicy;
import task2.engine.transform.Transformer;

import java.util.concurrent.*;

import static org.mockito.Mockito.*;

class ConsumerWorkerTest {

    @Test
    void retriesThreeTimesThenFails() throws Exception {

        Sink mockSink = mock(Sink.class);
        when(mockSink.type()).thenReturn(SinkType.DB);

        // Force sink to fail every time
        doThrow(new RuntimeException("fail"))
                .when(mockSink)
                .send(any());

        BlockingQueue<String> queue =
                new ArrayBlockingQueue<>(10);
        queue.put("test-record");

        Transformer transformer = record -> record.getBytes();
        RetryPolicy retry = new RetryPolicy(3);
        ThrottlePolicy throttle = mock(ThrottlePolicy.class);
        MetricsRegistry metrics = new MetricsRegistry();

        ConsumerWorker worker =
                new ConsumerWorker(
                        queue,
                        mockSink,
                        transformer,
                        retry,
                        throttle,
                        metrics
                );

        Thread t = new Thread(worker);
        t.start();

        Thread.sleep(200);
        t.interrupt();

        // Verify retry count
        verify(mockSink, times(3)).send(any());
    }
}
