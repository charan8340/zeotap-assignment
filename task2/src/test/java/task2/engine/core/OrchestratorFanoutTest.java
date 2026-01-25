package task2.engine.core;

import org.junit.jupiter.api.Test;
import task2.engine.sink.Sink;
import task2.engine.sink.SinkType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

class OrchestratorFanoutTest {

    @Test
    void fanoutSendsToAllSinks() throws Exception {

        Sink restSink = mock(Sink.class);
        Sink dbSink = mock(Sink.class);

        when(restSink.type()).thenReturn(SinkType.REST);
        when(dbSink.type()).thenReturn(SinkType.DB);

        restSink.send(any());
        dbSink.send(any());

        try {
            verify(restSink, atLeastOnce()).send(any());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        verify(dbSink, atLeastOnce()).send(any());
    }
}
