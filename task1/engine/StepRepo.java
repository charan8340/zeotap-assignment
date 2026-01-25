package task1.engine;

import java.sql.*;

public class StepRepo {

    private final Connection conn;
    public StepRepo() {
        this.conn = DBManager.getConnection();
    }

    public synchronized StepRecord find(String workflowId, String stepKey) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM steps WHERE workflow_id=? AND step_key=?");
            ps.setString(1, workflowId);
            ps.setString(2, stepKey);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new StepRecord(
                workflowId,
                stepKey,
                rs.getString("status"),
                rs.getString("output"),
                rs.getLong("updated_at")
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void insertRunning(String workflowId, String stepKey) {
        try {
            PreparedStatement ps = conn.prepareStatement("""
                INSERT OR IGNORE INTO steps
                VALUES (?, ?, 'RUNNING', NULL, ?)
            """);
            ps.setString(1, workflowId);
            ps.setString(2, stepKey);
            ps.setLong(3, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void markCompleted(String workflowId,
                                           String stepKey,
                                           String output) {
        update(workflowId, stepKey, "COMPLETED", output);
    }

    public synchronized void markFailed(String workflowId, String stepKey) {
        update(workflowId, stepKey, "FAILED", null);
    }

    private void update(String workflowId, String stepKey,
                        String status, String output) {
        try {
            PreparedStatement ps = conn.prepareStatement("""
                UPDATE steps
                SET status=?, output=?, updated_at=?
                WHERE workflow_id=? AND step_key=?
            """);
            ps.setString(1, status);
            ps.setString(2, output);
            ps.setLong(3, System.currentTimeMillis());
            ps.setString(4, workflowId);
            ps.setString(5, stepKey);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
