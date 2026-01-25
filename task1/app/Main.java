package task1.app;

import task1.engine.DurableEngine;
import task1.test.onboarding.EmployeeOnboardingWorkflow;

public class Main {
    public static void main(String[] args) {

        System.out.println("Workflow Started: ");
        System.out.println("Press CTRL+C anytime to simulate crash\n");

        DurableEngine engine = new DurableEngine("onboarding-workflow-1");

        EmployeeOnboardingWorkflow.run(engine);

        System.out.println("Workflow Finished");
    }
}