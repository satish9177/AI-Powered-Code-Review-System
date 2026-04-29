# AI-Powered Code Review System

This repository contains an MVP for an asynchronous AI code review system built with:

- React.js
- Spring Boot
- Kafka
- Claude API
- PostgreSQL

## Architecture

1. The React frontend submits code to the Spring Boot API.
2. The API stores the review as `PENDING` in PostgreSQL.
3. The API publishes a Kafka event with the review ID.
4. A Kafka consumer processes the event in the background.
5. The consumer calls the Claude API and stores the result.
6. The frontend polls `GET /api/reviews/{id}` until the review is complete.

## Project Structure

```text
backend/   Spring Boot API, Kafka consumer/producer, Claude integration
frontend/  React + Vite UI for submission and polling
```

## Backend Endpoints

- `POST /api/reviews` submits code for review
- `GET /api/reviews/{id}` retrieves status and result

## Review Lifecycle

- `PENDING`
- `PROCESSING`
- `COMPLETED`
- `FAILED`

## Local Run Plan

### 1. Start PostgreSQL and Kafka

```bash
docker compose up -d
```

### 2. Configure Claude API key

Set the environment variable:

```bash
ANTHROPIC_API_KEY=your_key_here
```

### 3. Run the backend

```bash
cd backend
mvn spring-boot:run
```

### 4. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:5173` and proxies API requests to `http://localhost:8080`.

## Next Build Steps

1. Add structured Claude JSON output and richer frontend rendering.
2. Add retry observability and admin visibility for DLT failures.
3. Add syntax highlighting and result sections in the UI.
4. Add tests for controller, service, and Kafka processing.
