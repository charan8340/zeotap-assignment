package task1.engine;

import java.util.concurrent.Callable;

public class DurableEngine {

    private final DurableContext context;
    private final StepRepo repo;

    public DurableEngine(String workflowId) {
        this.context = new DurableContext(workflowId);
        this.repo = new StepRepo();
    }

    public <T> T step(String name, Class<T> type, Callable<T> fn) {

        long seq = context.nextSequence();
        String stepKey = name + ":" + seq;

        System.out.println("STEP: " + stepKey);

        StepRecord record = repo.find(context.getWorkflowId(), stepKey);

        if (record != null && record.isCompleted()) {
            System.out.println("SKIPPED (already completed)");
            return JsonUtil.deserialize(record.output, type);
        }

        if (record != null && record.isZombie()) {
            System.out.println("ZOMBIE detected → retrying");
            repo.markFailed(context.getWorkflowId(), stepKey);
        }

        repo.insertRunning(context.getWorkflowId(), stepKey);

        try {
            T result = fn.call();
            repo.markCompleted(context.getWorkflowId(),
                               stepKey,
                               JsonUtil.serialize(result));
            System.out.println("COMPLETED");
            return result;
        } catch (Exception e) {
            repo.markFailed(context.getWorkflowId(), stepKey);
            throw new RuntimeException(e);
        }
    }
}
