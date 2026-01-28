# Zeotap Backend Engineering Assignments

This repository contains **both backend engineering assignments** completed as part of the Zeotap assignment.

Each assignment is implemented as a **self-contained project** with its own source code, tests, configuration, and detailed documentation.

The intent of this repository is to demonstrate:
- Systems design thinking
- Concurrency correctness
- Streaming & memory-safe processing
- Durability and fault tolerance

---

## Repository Structure

```

.
├── task1/   → Assignment 1: Durable Execution Engine
├── task2/   → Assignment 2: High-Throughput Fan-Out & Transformation Engine
└── README.md 


```

Each folder contains:
- A complete implementation
- A dedicated `README.md` explaining architecture and design decisions
- Sample workflows / inputs
- Tests and prompts used during development

---

## Assignment 1: Durable Execution Engine (`task1/`)

**Problem solved:**  
Build a native durable execution engine that allows developers to write *normal Java code* (loops, conditionals, concurrency) while ensuring completed side effects are **not re-executed after crashes**.

**Key highlights:**
- Replay-based durability (no checkpoints, no DSLs)
- Persistent memoization using SQLite
- Automatic sequence ID generation for loops and concurrency
- Zombie step detection and deterministic retries
- Parallel step execution with thread-safe persistence

**Detailed design, code, and execution instructions are documented inside `task1/README.md`.**

---

## Assignment 2: High-Throughput Fan-Out & Transformation Engine (`task2/`)

**Problem solved:**  
Design a streaming data pipeline that reads very large files (100GB+), transforms records into sink-specific formats, and dispatches them concurrently without overwhelming downstream systems.

**Key highlights:**
- True streaming ingestion (constant memory usage)
- Bounded queues with backpressure
- Sink-specific transformations using Strategy pattern
- Config-driven throttling and retry logic
- Parallel sink processing with clear observability
- Designed to run safely with `-Xmx512m`

**Full architecture, configuration, and testing details are available in `task2/README.md`.**

## Guide To view the assignments

This repository contains two independent assignments, each documented in its respective folder:
- **`task1/`** contains the Durable Execution Engine implementation, including design rationale, workflow examples, and recovery behavior.
- **`task2/`** contains the High-Throughput Fan-Out & Transformation Engine, covering streaming ingestion, concurrency, backpressure, and observability.
- 
Each folder includes a dedicated `README.md` with detailed explanations, setup instructions, and relevant implementation notes.

---

## Notes

- Both assignments are implemented in **Java** using standard concurrency primitives.
- No external orchestration frameworks or domain-specific languages were used.
- All prompts utilized during design and development are included, as requested.

Thank you for taking the time to review this submission.
