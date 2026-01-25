package task1.test.onboarding;

import task1.engine.DurableEngine;
import java.util.concurrent.CompletableFuture;

public class EmployeeOnboardingWorkflow {

    public static void run(DurableEngine engine) {
        
        engine.step("create-employee", String.class, () -> {
            System.out.println("Creating employee record...");
            Thread.sleep(3000);
            return "EMP-101";
        });

        CompletableFuture.allOf(
            CompletableFuture.runAsync(() ->
                engine.step("provision-laptop", Void.class, () -> {
                    System.out.println("Provisioning laptop...");
                    Thread.sleep(5000);
                    return null;
                })
            ),
            CompletableFuture.runAsync(() ->
                engine.step("provision-access", Void.class, () -> {
                    System.out.println("Provisioning access...");
                    Thread.sleep(5000);
                    return null;
                })
            )
        ).join();

        engine.step("send-email", Void.class, () -> {
            System.out.println("Sending welcome email...");
            Thread.sleep(3000);
            return null;
        });
    }
}
