package task1.engine;

import java.util.concurrent.atomic.AtomicLong;

public class DurableContext {

    private final String workflowId;
    private final AtomicLong sequence = new AtomicLong(0);

    public DurableContext(String workflowId) {
        this.workflowId = workflowId;
    }

    public String getWorkflowId() {
        return workflowId;
    }

    public long nextSequence() {
        return sequence.incrementAndGet();
    }
}
