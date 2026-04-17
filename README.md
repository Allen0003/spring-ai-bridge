# Spring AI Bridge: Agentic Financial Operations Platform

A next-generation financial transaction and AI-enhanced operations (AIOps) platform built with a Cloud-Native architecture. This project demonstrates how to evolve traditional Spring Boot microservices into **Agentic AI** systems capable of autonomous task execution, intelligent log analysis, and RAG-based knowledge retrieval.

## 🎯 Core Objectives

* **Agentic AI Implementation**: Developing AI Agents that can autonomously monitor system health, analyze failure logs, and provide actionable remediation steps via Function Calling.
* **RAG (Retrieval-Augmented Generation)**: Integrating Vector Databases to allow the LLM to access internal financial regulations and transaction SOPs for precise querying.
* **Intelligent Automation**: Enhancing existing automated alerting systems with AI reasoning to reduce Mean Time to Recovery (MTTR).
* **Cloud-Native Resilience**: Leveraging AWS EKS, Kafka, and KEDA to ensure high availability and scalability for both financial workloads and AI processing.

## 🛠 Tech Stack

* **Backend**: Java 21 / Spring Boot 3.4+
* **AI Orchestration**: Spring AI / LangChain4j
* **LLMs**: OpenAI GPT-4o / Ollama (Local Development)
* **Data Storage**: MySQL (Transactional), pgvector / Milvus (Vector Store)
* **Infrastructure**: Docker, Kubernetes (AWS EKS), GitHub Actions (CI/CD)
* **Messaging**: Apache Kafka (Event-Driven Architecture)

## 🧠 Key Features to Implement

### 1. Log Analysis Agent (AIOps)
An AI agent designed to monitor Kafka lag and transaction exceptions. It utilizes **Function Calling** to interact with existing microservice APIs, performing root-cause analysis on system failures.

### 2. Financial Knowledge RAG
A retrieval system that vectorizes internal documents. It allows operators to query complex transaction rules or user-specific rejection reasons using natural language.

### 3. AI-Powered Self-Healing
Integration with KEDA and Kubernetes metrics to provide AI-suggested scaling policies and automated system restarts based on anomaly detection in logs.

## 🚦 Getting Started

### Prerequisites
* JDK 21 or higher
* Docker & Docker Compose
* OpenAI API Key or Local Ollama instance