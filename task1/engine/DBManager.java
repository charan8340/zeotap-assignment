package task1.engine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBManager {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:onboarding.db");
                init();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    private static void init() throws Exception {
        Statement stmt = connection.createStatement();
        stmt.execute("""
            CREATE TABLE IF NOT EXISTS steps (
                workflow_id TEXT,
                step_key TEXT,
                status TEXT,
                output TEXT,
                updated_at INTEGER,
                PRIMARY KEY (workflow_id, step_key)
            )
        """);
    }
}
