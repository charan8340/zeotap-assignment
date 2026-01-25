# Durable Execution Engine (Assignment 1)

---

## Overview

This project implements a **Durable Execution Engine** in Java.

The goal is to allow developers to write **normal, idiomatic Java code**—using loops, conditionals, and concurrency—and make that code *durable*. Durability here means the application can crash at any point (process kill, CTRL+C, power loss) and, upon restart, resume execution **without re-running previously completed side effects**.
---

## Core Idea: Replay + Memoization

The engine does **not** snapshot memory or resume execution from an intermediate program counter.

Instead:

- Workflow code is re-executed from the beginning after every restart
- All side-effectful operations are wrapped in a `step(...)`
- Completed steps are recorded in persistent storage (SQLite)
- On replay:
  - Previously completed steps are skipped
  - Cached results are returned
  - Side effects are not re-executed

This replay-based model ensures correctness while keeping the programming model simple and explicit.

---

## Project Structure

```

task1/
├── engine/
│   ├── DurableContext.java
│   ├── DurableEngine.java
│   ├── StepRepo.java
│   ├── StepRecord.java
│   ├── DBManager.java
│   └── JsonUtil.java
│
├── test/
│   └── onboarding/
│       └── EmployeeOnboardingWorkflow.java
│
├── app/
│   └── Main.java
│
├── workflow.db        (created at runtime)
├── README.md
└── Prompts.txt

````

---

## Core Architecture

### 1. Step Primitive

All important side effects must be wrapped using a `step` primitive:

```java
<T> T step(String stepName, Class<T> type, Callable<T> fn)
````

**Design intent:**
This makes side effects explicit, auditable, and replay-safe without restricting how workflows are written.

---

### 2. Memoization via Persistent Store(Database)

Before executing a step, the engine checks persistent storage:

* If the step was already completed for this workflow:

  * Execution is skipped
  * The cached result is deserialized and returned
* Otherwise:

  * The step function is executed
  * The result is persisted

This ensures idempotent behavior across crashes and restarts.

---

### 3. Sequence Management (Logical Clock)

To support loops, retries, and repeated step names, each step execution is assigned a **sequence ID**.

* Implemented using `AtomicLong`
* Each invocation of `step()` increments the logical clock

Final step key format:

```
step_key = stepName + ":" + sequenceId
```

This guarantees uniqueness even when:

* Step names repeat
* Steps execute inside loops
* Steps execute concurrently

**Trade-off:**
This avoids forcing users to manually generate unique identifiers while keeping the execution model deterministic.

---

## Persistence Layer
---

### Steps Table Schema
---

### Column Semantics

| Column      | Purpose                                |
| ----------- | -------------------------------------- |
| workflow_id | Identifies a workflow instance         |
| step_key    | Unique execution identifier            |
| status      | RUNNING / COMPLETED / FAILED           |
| output      | Serialized step result                 |
| updated_at  | Timestamp for crash / zombie detection |

---

### Zombie Step Handling

A **zombie step** occurs when:

* A step is marked `RUNNING`
* The process crashes before it is marked `COMPLETED`

On restart:

* `RUNNING` steps with stale timestamps are treated as zombies
* These steps are deterministically retried

**Design rationale:**
Exactly-once execution is impossible without transactional side effects.
This engine embraces **at-least-once execution with deterministic replay**, which is the same compromise made by most real-world workflow systems.

---

## Concurrency Support

* Parallel steps are supported using `CompletableFuture`
* Sequence IDs are generated using `AtomicLong` (thread-safe)
* SQLite writes are synchronized inside `StepRepo` to avoid `SQLITE_BUSY`

Example:

```java
CompletableFuture.allOf(
    CompletableFuture.runAsync(() -> engine.step(...)),
    CompletableFuture.runAsync(() -> engine.step(...))
).join();
```

This allows concurrent execution while preserving replay determinism.

---

## Serialization Strategy

To store the result of objects and to compare whether it is completed or not we use serialization and de-serialization

## Example Workflow: Employee Onboarding

Implemented in:

```
task1/test/onboarding/EmployeeOnboardingWorkflow.java
```

Workflow steps:

1. Create Employee Record (sequential)
2. Provision Laptop (parallel)
3. Provision Access (parallel)
4. Send Welcome Email (sequential)

The workflow demonstrates:

* Loops (multiple employees)
* Parallel execution inside loops
* Safe recovery after crashes

---

## CLI Application

Entry point:

```
task1/app/Main.java
```

Features:

* Starts the workflow
* Allows crash simulation via CTRL+C
* On restart, skips completed steps and continues execution

This provides an end-to-end demonstration of durability.

---

## How to Run

### Compile

```bash
curl -O https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.36.0.3/sqlite-jdbc-3.36.0.3.jar
javac -cp .:sqlite-jdbc-3.36.0.3.jar $(find task1 -name "*.java")
```

### Run

```bash
java -cp .:sqlite-jdbc-3.36.0.3.jar task1.app.Main
```

### Reset Database

```bash
rm workflow.db
```

---

## Assignment Requirement Mapping

### Functional Requirements

| Requirement     | Status                             |
| --------------- | ---------------------------------- |
| Workflow Runner | Implemented via `Main.java`        |
| Step Primitive  | `DurableEngine.step()`             |
| Resilience      | Completed steps skipped on restart |
| Concurrency     | Supported via `CompletableFuture`  |
| Thread Safety   | SQLite writes synchronized         |

---

### Persistence Requirements

| Requirement       | Status                   |
| ----------------- | ------------------------ |
| RDBMS usage       | SQLite                   |
| Steps table       | Implemented as specified |
| Serialized output | Stored and replayed      |

---

### Technical Constraints

| Constraint    | Status                         |
| ------------- | ------------------------------ |
| Type safety   | Generics used                  |
| Serialization | Deterministic, simple          |
| No DSL        | Workflow written in plain Java |

---

## Bonus: Automatic Sequence IDs

* Sequence IDs are generated automatically using `AtomicLong`
* Users do not need to supply unique step identifiers
* Works correctly across loops and concurrent execution
