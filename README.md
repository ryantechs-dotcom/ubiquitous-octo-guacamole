# Trading System for Order Management and Market Tracking

**Duration:** January 2025 – March 2025  
**Technologies:** Java, Object-Oriented Programming (OOP), Design Patterns (Facade, Flyweight, Observer)

## 📌 Overview

This project is a modular trading system built in Java, designed to manage users, track orders, and observe market activity in real time. It leverages key OOP principles and design patterns to ensure scalability, maintainability, and performance.

## 🧱 Features

- **User Management**: Handles registration, authentication, and user roles within the trading system.
- **Order Tracking**: Supports the creation, management, and tracking of buy/sell orders.
- **Market Observation**: Implements observer pattern for real-time market updates.
- **Custom Exception Handling**: Improves system reliability with tailored exceptions like `InvalidPriceException` and `InvalidOrderException`.

## 🛠️ Design Patterns Implemented

- **Facade Pattern**: Provides a simplified interface to complex subsystems, reducing coupling and increasing clarity.
- **Flyweight Pattern**: Minimizes memory usage by reusing immutable data structures for commonly repeated elements (e.g., orders and prices).
- **Observer Pattern**: Enables modules to subscribe to and react to market state changes, supporting real-time updates.

## 📘 Modules

- `UserManager`: Manages user sessions and permissions.
- `ProductBook`: Maintains active orders and market quotes for individual products.
- `MarketDataPublisher`: Publishes market events and notifies registered observers.
- `OrderValidator`: Validates incoming orders against custom business rules.
- `ExceptionHandler`: Manages domain-specific exceptions to prevent system crashes.

## ✅ Key Highlights

- Designed with SOLID principles for clear separation of concerns and modular architecture.
- Ensured stability with a comprehensive exception handling framework.
- Optimized performance and memory usage through strategic use of design patterns.

## 🚀 Getting Started

1. Clone the repository.
2. Compile the Java source files:
   ```bash
   javac *.java
