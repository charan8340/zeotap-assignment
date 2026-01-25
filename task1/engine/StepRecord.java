package task1.engine;

public class StepRecord {

    public final String workflowId;
    public final String stepKey;
    public final String status;
    public final String output;
    public final long updatedAt;

    public StepRecord(String workflowId, String stepKey,
                      String status, String output, long updatedAt) {
        this.workflowId = workflowId;
        this.stepKey = stepKey;
        this.status = status;
        this.output = output;
        this.updatedAt = updatedAt;
    }

    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    public boolean isRunning() {
        return "RUNNING".equals(status);
    }

    public boolean isZombie() {
        return isRunning() &&
               (System.currentTimeMillis() - updatedAt > 5000);
    }
}
