# Assignement 2: High-Throughput Fan-Out & Transformation Engine

---

## Overview

This project implements a **Distributed Data Fan-Out & Transformation Engine** using Java.

In modern data architectures, a single bulk data source (such as a flat-file export) often needs to be propagated to multiple downstream systems like REST APIs, gRPC services, message queues, or databases. Each downstream system may require data in a different format and may impose its own throughput limits.

The objective of this assignment is to design and build a system that:

- Processes very large input files efficiently
- Transforms records into sink-specific formats
- Dispatches records concurrently to multiple sinks
- Prevents downstream overload using throttling and backpressure
- Provides clear observability into throughput and failures
- Scales with CPU resources while maintaining low memory usage

---

## High-Level Design

```
                ┌────────────┐
                │ Input File │  (CSV / JSONL)
                └─────┬──────┘
                      │
                ┌─────▼──────┐
                │  Producer  │  (single thread, streaming)
                └─────┬──────┘
                      │  fan-out
        ┌─────────────┼────────────────┬────────────────┬──────────────┐
        ▼             ▼                ▼                ▼              ▼
  BlockingQueue   BlockingQueue   BlockingQueue   BlockingQueue   (bounded)
    (REST)           (gRPC)          (MQ)            (DB)
        │             │                │                │
   Consumer(s)    Consumer(s)     Consumer(s)     Consumer(s)
        │             │                │                │
   Transformer     Transformer      Transformer      Transformer
        │             │                │                │
   REST Sink       gRPC Sink       MQ Sink          DB Sink


Input File
    |
    v
Producer (single thread and single pointer to the file)
    |
    v
Fan-Out to Bounded Queues(seperate queues for differnt sink)
    |
    v
Concurrent Sink Workers
    |
    v
REST | gRPC | Message Queue | Database

````

The system is designed as a pipeline, where each stage has a single responsibility and communicates with each other.

---

## Design Decisions (Mapped to Requirements)

### 1. Ingestion Layer (Streaming Producer)

- Input data is processed incrementally, one record at a time
- The entire file is never loaded into memory
- Enables efficient processing of very large files (100GB+)

**Why this design:**  
Streaming ingestion guarantees predictable memory usage and ensures the application can run with a small heap size regardless of file size.

---

### 2. Transformation Layer

- Input records are treated as raw data
- Each downstream system expects data in a different format
- Records are transformed into:
  - HTTP-compatible formats for REST services
  - gRPC-compatible formats
  - Message-oriented formats for queues
  - Structured formats for wide-column databases

**Key idea:**  
Transformation is isolated from ingestion and distribution, ensuring clean separation of responsibilities and easy extensibility.

---

### 3. Concurrency Model

- Each sink runs independently and concurrently
- Multiple worker threads are created per sink
- A single producer feeds all sinks without blocking fast consumers

This is achieved using:
- `ExecutorService` for managing worker threads
- `BlockingQueue` for safe communication between producer and consumers

**Why this matters:**
- Sinks do not block each other
- Slow sinks do not affect fast ones
- The system scales naturally with available CPU cores

---

### 4. Backpressure & Memory Safety

- Each sink is assigned a bounded queue
- If a sink becomes slow:
  - Its queue fills up
  - The producer automatically blocks

This prevents unbounded buffering and memory exhaustion.

**Result:**  
The system remains stable under load and avoids `OutOfMemoryError`s.

---

### 5. Throttling & Retry Handling

- Each sink has a configurable rate limit
- Requests are throttled to protect downstream systems
- Failed records are retried up to a configurable maximum
- Success and failure outcomes are tracked for observability

---

### 6. Observability

At a fixed interval, the system prints:

- Total records processed
- Throughput (records per second)
- Success and failure counts per sink

This provides continuous visibility into system health during execution.

---

## Memory Management & Heap Size

The application is explicitly designed to run with a small and fixed heap size, independent of input file size.

To configure heap size at runtime:

```bash
java -Xmx512m ...
````

Because:

* Records are processed one at a time
* Queues are bounded
* No full-file buffering is used

Memory usage remains stable even for very large input files.

---

## Configuration

All runtime behavior is controlled via external configuration.

You can configure:

* Input file path
* Sink enablement
* Rate limits
* Queue capacities
* Worker thread counts
* Retry limits
* Metrics reporting interval

This ensures the system is fully configurable without code changes.

---

## Project Structure

```
task2/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── task2/engine/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data/
│   │           └── input.jsonl
│   └── test/
│       └── java/
│           └── task2/engine/
```

---

## How to Build and Run

### Prerequisites

* Java 21
* Maven

### Build

```bash
mvn clean package
```

### Run

```bash
java -Xmx512m -cp target/classes task2.engine.Main
```

The application will start processing the input file and periodically print metrics to the console.

---

## Testing Strategy

* Unit tests validate data transformations
* Integration tests validate fan-out behavior and retry logic
* External systems are mocked to ensure deterministic and isolated tests

Testing focuses on correctness, resilience, and concurrency behavior.

---

## Assumptions

* Input files are line-delimited (CSV or JSONL)
* Downstream systems are simulated using mock sinks
* Rate limits represent approximate behavior

---
