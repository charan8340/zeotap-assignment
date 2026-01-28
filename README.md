# Branch Customer–Agent Chat Application

This repository contains a **customer–agent chat application** built as part of the Branch assignment.

The application is implemented as a **self-contained full-stack project** with its own frontend, backend, database, container configuration, and documentation.

The intent of this repository is to demonstrate:
- Full-stack system design
- Real-time customer–agent interaction
- Containerized development workflows
- Clean API design and persistence handling

---

## Repository Structure

```

branch-chat-app/
├── backend/           → Node.js (Express) API & MongoDB integration
├── frontend/          → React (Vite) customer & agent chat UI
├── docker-compose.yml → Multi-container setup
└── README.md

````

Each major component contains:
- A complete implementation
- Clear separation of responsibilities
- Configuration suitable for Docker-based execution

---

## Application Overview

**Problem solved:**  
Build a simple customer–agent chat system where customers can initiate conversations and agents can respond through a dedicated interface.

The application consists of:
- A customer-facing chat UI
- An agent-facing chat UI
- A backend API to manage message flow
- MongoDB for persistent message storage

All components run together using Docker Compose.

---

## Tech Stack

- **Frontend:** React (Vite)
- **Backend:** Node.js (Express)
- **Database:** MongoDB
- **Containerization:** Docker & Docker Compose

---

## Prerequisites

Only Docker is required.

### Install Docker

- **Windows / macOS:** https://www.docker.com/products/docker-desktop  
- **Linux:** https://docs.docker.com/engine/install/

Verify installation:

```bash
docker --version
docker compose version
````

---

## Getting the Project

### Clone the Repository

```bash
git clone https://github.com/charan8340/branch-chat-application
cd branch-chat-app
```

### Or Download ZIP

Download the ZIP from GitHub and extract it locally.

---

## Build and Run the Application

Build and start all services:

```bash
docker compose up --build -d
```

---

## Application Access

### Frontend

* **Customer Chat UI:**
  [http://localhost:5173](http://localhost:5173)
  Customers can enter their name and number and start chatting with an agent.

* **Agent Chat UI:**
  [http://localhost:5173/agent](http://localhost:5173/agent)
  Agents can respond to incoming customer messages.

### Backend API

* **Base URL:**
  [http://localhost:4000](http://localhost:4000)

---

## API Usage (Postman / cURL)

### Send Message

**Endpoint:**

```
POST http://localhost:4000/api/message
```

**Request Body Example:**

```json
{
  "customerId": 87456,
  "customerName": "Dhar",
  "text": "Hello how are you doing?"
}
```

---

## Logs and Container Status

### View Backend Logs

```bash
docker logs -f branch-backend
```

### List Running Containers

```bash
docker ps
```

---

## Stopping the Application

### Stop Containers

```bash
docker compose down
```

### Stop Containers and Remove Database Data

```bash
docker compose down -v
```

---

## Notes

* MongoDB runs inside a Docker container.
* No local installation of Node.js or MongoDB is required.

Thank you for taking the time to review this submission.
