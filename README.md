# EPAM Java Backend Interview – Round 2 🎯

Complete preparation guide for EPAM Round 2 with real interview questions, code examples, and detailed explanations.

**📺 YouTube Channel**: [Dip Developer](https://www.youtube.com/@DipDeveloper)  
**📦 GitHub Repository**: [TheDipDeveloper/epam-java-interview-round2](https://github.com/TheDipDeveloper/epam-java-interview-round2)

---

## 📊 Overview

This repository contains:
- ✅ **Actual Round 2 Questions** asked to real candidates
- ✅ **Code Implementations** with detailed comments
- ✅ **Theoretical Explanations** with diagrams
- ✅ **Real-World Examples** (Paytm-like service)
- ✅ **System Design** interviews
- ✅ **Design Patterns** explained with code

**Result:** 📺 Used for YouTube video "EPAM Round 2 Interview Questions (Real Questions)" on Dip Developer channel

---

## 📚 Questions Covered

### 1. **About Yourself & Work** 👨‍💼
📄 [`AboutYourself.md`](./AboutYourself.md)
- How to introduce yourself
- Key points to cover
- Soft skills to highlight

---

### 2. **Microservices** 🏗️

| Question | Resource | Type |
|----------|----------|------|
| **What are benefits of microservices?** | [`BenefitsOfMicroservices.md`](./BenefitsOfMicroservices.md) | Theory |
| **Difference between Monolithic and Microservices** | [`microservices/MonolithicVsMicroservices.md`](./microservices/MonolithicVsMicroservices.md) | Theory |
| **What is Circuit Breaker?** | [`microservices/CircuitBreaker.md`](./microservices/CircuitBreaker.md) | Theory |
| **What is SAGA Pattern?** | [`microservices/SagaPattern.md`](./microservices/SagaPattern.md) | Theory |
| **How to scale a microservice?** | [`microservices/ScalingMicroservice.md`](./microservices/ScalingMicroservice.md) | Theory |

#### Code Examples:
- 🔌 [`src/main/java/com/epam/microservices/CircuitBreakerImpl.java`](./src/main/java/com/epam/microservices/CircuitBreakerImpl.java) - Circuit Breaker Pattern (Resilience4j)

---

### 3. **Design Patterns** 🎭

| Question | Resource | Type |
|----------|----------|------|
| **What is behavioral design pattern?** | [`design-patterns/DesignPatterns.md`](./design-patterns/DesignPatterns.md) | Theory |
| **Different types of design patterns** | [`design-patterns/DesignPatterns.md`](./design-patterns/DesignPatterns.md) | Theory |

#### Code Examples:
- 💳 [`design-patterns/StrategyPattern.java`](./design-patterns/StrategyPattern.java) - Strategy Pattern with payment methods (Credit Card, UPI, Wallet, Net Banking)

---

### 4. **System Design** 🏛️

| Question | Resource | Type |
|----------|----------|------|
| **What is SOLID principles?** | [`system-design/SOLID.md`](./system-design/SOLID.md) | Theory |
| **What is CAP Theorem?** | [`system-design/CAP-Theorem.md`](./system-design/CAP-Theorem.md) | Theory |
| **What is Polyglot Persistence?** | [`system-design/PolyglotPersistence.md`](./system-design/PolyglotPersistence.md) | Theory |
| **Design Paytm Service** | [`DesigningPaytm.md`](./DesigningPaytm.md) | System Design |

#### Code Examples:
- 🗄️ [`src/main/java/com/epam/comparisons/MongoDBvsMysql.java`](./src/main/java/com/epam/comparisons/MongoDBvsMysql.java) - MongoDB vs MySQL comparison with real scenarios

---

### 5. **Database** 🗄️

| Question | Resource | Type |
|----------|----------|------|
| **3rd Highest Salary (SQL Query)** | [`database/Top3Salary.sql`](./database/Top3Salary.sql) | SQL |
| **What is 1 at the end in query?** | [`database/Top3Salary.sql`](./database/Top3Salary.sql) | SQL |
| **Why MongoDB better than MySQL?** | [`src/main/java/com/epam/comparisons/MongoDBvsMysql.java`](./src/main/java/com/epam/comparisons/MongoDBvsMysql.java) | Comparison |

---

### 6. **Tools** 🔧

| Question | Resource | Type |
|----------|----------|------|
| **Do you know Kafka?** | [`tools/Kafka.md`](./tools/Kafka.md) | Theory |
| **Do you know Jenkins?** | [`tools/Jenkins.md`](./tools/Jenkins.md) | Theory |

---

## 📂 Project Structure

```
epam-java-interview-round2/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
│
├── AboutYourself.md                # Introduction & background
├── BenefitsOfMicroservices.md       # Microservices advantages
├── DesigningPaytm.md               # System design interview (Paytm service)
│
├── design-patterns/
│   ├── DesignPatterns.md           # All design patterns explained
│   └── StrategyPattern.java        # Code: Strategy pattern (Payment methods)
│
├── microservices/
│   ├── CircuitBreaker.md           # Circuit breaker pattern
│   ├── MonolithicVsMicroservices.md # Architecture comparison
│   ├── SagaPattern.md              # Distributed transactions
│   └── ScalingMicroservice.md      # Scaling strategies
│
├── system-design/
│   ├── SOLID.md                    # SOLID principles (SRP, OCP, LSP, ISP, DIP)
│   ├── CAP-Theorem.md              # Consistency, Availability, Partition
│   └── PolyglotPersistence.md      # Multiple database types
│
├── database/
│   └── Top3Salary.sql              # SQL query examples
│
├── tools/
│   ├── Kafka.md                    # Kafka messaging system
│   └── Jenkins.md                  # Jenkins CI/CD
│
└── src/main/java/com/epam/
    ├── microservices/
    │   └── CircuitBreakerImpl.java  # Circuit breaker implementation (Resilience4j)
    │
    └── comparisons/
        └── MongoDBvsMysql.java     # MongoDB vs MySQL scenarios
```

---

## 🎓 How to Use This Repo

### For Interview Preparation:
1. **Read Theory**: Start with markdown files to understand concepts
2. **Study Code**: Look at implementation examples
3. **Practice**: Try to implement patterns yourself
4. **System Design**: Practice designing Paytm-like service

### For Learning:
- Each markdown has diagrams and real-world examples
- Code files have detailed comments explaining logic
- SQL file includes multiple solutions with explanations

### For Teaching/YouTube:
- Use markdown as script for video
- Use code examples for demos
- Use diagrams for visual explanation

---

## 🚀 Quick Start

### Prerequisites:
```bash
- Java 11+
- Maven 3.6+
- Git
```

### Setup:
```bash
git clone <repo-url>
cd epam-java-interview-round2
mvn clean install
```

### Run Examples:
```bash
# Run Strategy Pattern Demo
mvn exec:java -Dexec.mainClass="com.dipdeveloper.designpatterns.StrategyPatternDemo"

# Run Circuit Breaker Demo
mvn exec:java -Dexec.mainClass="com.dipdeveloper.microservices.CircuitBreakerDemo"
```

---

## 📝 Key Concepts Covered

### Microservices
- ✅ Monolithic vs Microservices
- ✅ Benefits of microservices
- ✅ Scaling strategies
- ✅ Circuit breaker pattern
- ✅ SAGA pattern for distributed transactions

### Design Patterns
- ✅ Creational (Singleton, Factory, Builder)
- ✅ Structural (Decorator, Adapter, Proxy)
- ✅ Behavioral (Strategy, Observer, Command)

### System Design
- ✅ SOLID principles
- ✅ CAP theorem
- ✅ Polyglot persistence
- ✅ Database selection criteria

### Databases
- ✅ SQL optimization
- ✅ MongoDB vs MySQL
- ✅ Distributed transactions
- ✅ Scaling databases

### Tools
- ✅ Kafka (event streaming)
- ✅ Jenkins (CI/CD)
- ✅ Docker & Kubernetes

---

## 💡 Interview Tips

### Before Interview:
- ✅ Understand **why** before **what**
- ✅ Know trade-offs between solutions
- ✅ Have real-world examples ready
- ✅ Practice system design sketches

### During Interview:
- ✅ Ask clarifying questions
- ✅ Start with simple solution, then optimize
- ✅ Discuss trade-offs explicitly
- ✅ Show knowledge of tools/frameworks
- ✅ Think about scalability from start

### Common Questions:
```
Q: What would you do differently?
A: Think about scale, consistency, availability

Q: Why MongoDB and not MySQL here?
A: Flexible schema, horizontal scaling, nested data...

Q: How would you handle failures?
A: Circuit breaker, retries, fallbacks, idempotency...

Q: What about security?
A: Encryption, authentication, input validation...
```

---

## 🎯 Topics by Difficulty

### Beginner Friendly:
- About Yourself
- Design Patterns
- MongoDB vs MySQL
- Jenkins basics

### Intermediate:
- Monolithic vs Microservices
- Circuit Breaker
- SOLID Principles
- CAP Theorem

### Advanced:
- SAGA Pattern
- Scaling Strategies
- CAP Theorem trade-offs
- System Design (Paytm service)
- Polyglot Persistence

---

## 📊 Statistics

- **Total Questions**: 13+
- **Markdown Files**: 11
- **Code Files**: 3
- **SQL Queries**: 4 approaches
- **Real-World Examples**: 10+
- **Diagrams**: 20+

---

## 🎬 Video Reference

This content is designed for YouTube video:
- **Title**: "EPAM Java Backend Interview Round 2 - Real Questions & Answers"
- **Duration**: ~45-50 minutes
- **Coverage**: All questions mentioned in this README

---

## 🤝 Contributing

Found issues or want to add more questions?
- Create issues for bugs/clarifications
- Submit PRs for improvements
- Add more real interview questions

---

## 📄 License

This content is created for educational purposes. Feel free to use for:
- ✅ Interview preparation
- ✅ Learning
- ✅ Teaching
- ✅ YouTube videos

---

## 🎓 Final Advice

**Remember**: Interviewers don't expect you to know everything. They want to see:

1. **Problem-solving**: Can you think through complex problems?
2. **Communication**: Can you explain your ideas clearly?
3. **Trade-offs**: Do you understand pros/cons?
4. **Learning**: Can you learn new concepts?
5. **Experience**: Do you have real-world examples?

Focus on understanding **why**, not just memorizing **what**.

---

**Good luck with your EPAM interview! 🚀**

---

## 📞 Connect With Us

- **YouTube Channel**: [Dip Developer](https://www.youtube.com/@DipDeveloper)
- **GitHub Repository**: [TheDipDeveloper/epam-java-interview-round2](https://github.com/TheDipDeveloper/epam-java-interview-round2)
- **Video**: EPAM Java Backend Interview Round 2 - Real Questions & Answers

Subscribe to Dip Developer for more interview preparation content! 🎬

---

## 📊 Quick Links

- [All Questions Overview](#-questions-covered)
- [Project Structure](#-project-structure)
- [Quick Start Guide](#-quick-start)
- [Interview Tips](#-interview-tips)

---

**Last Updated**: March 2026  
**Version**: 2.0 (Round 2 Complete)  
**Created By**: Dip Developer  
**License**: MIT (Open Source)
