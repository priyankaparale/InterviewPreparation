package com.dipdeveloper.comparisons;

/**
 * MongoDB vs MySQL Comparison
 *
 * Question: Why is MongoDB better than MySQL for certain use cases?
 */

public class MongoDBvsMysqlComparison {

    /**
     * ============= SCENARIO 1: E-commerce Product Catalog =============
     *
     * Problem with MySQL:
     * - Different products have different attributes
     * - Electronics: brand, model, specs
     * - Clothing: size, color, material
     * - Books: author, pages, ISBN
     *
     * MySQL approach:
     * CREATE TABLE products (
     *     product_id INT,
     *     name VARCHAR(100),
     *     price DECIMAL,
     *     brand VARCHAR(50),          // Only for electronics
     *     model VARCHAR(50),           // Only for electronics
     *     size VARCHAR(10),            // Only for clothing
     *     color VARCHAR(20),           // Only for clothing
     *     author VARCHAR(100),         // Only for books
     *     pages INT                    // Only for books
     * );
     *
     * Problem:
     * - Many NULL values (wasted space)
     * - Hard to add new product types
     * - Fixed schema not flexible
     *
     * MongoDB approach:
     * {
     *     _id: ObjectId(),
     *     name: "iPhone 14",
     *     price: 79999,
     *     // Only include what's needed
     *     type: "Electronics",
     *     brand: "Apple",
     *     model: "A2629",
     *     specs: {
     *         processor: "A15 Bionic",
     *         camera: "48MP",
     *         battery: "3200mAh"
     *     }
     * }
     *
     * {
     *     _id: ObjectId(),
     *     name: "Cotton T-Shirt",
     *     price: 499,
     *     type: "Clothing",
     *     size: "M",
     *     color: "Blue",
     *     material: "Cotton"
     * }
     *
     * Benefits:
     * ✅ No NULL values
     * ✅ Easy to add new attributes
     * ✅ Flexible for different product types
     * ✅ Faster queries (no unnecessary columns)
     */

    /**
     * ============= SCENARIO 2: User Profiles with Nested Data =============
     *
     * Problem with MySQL:
     * - User has multiple addresses (Home, Office, Billing)
     * - Need separate table + JOIN
     *
     * MySQL approach:
     * CREATE TABLE users (
     *     user_id INT PRIMARY KEY,
     *     name VARCHAR(50)
     * );
     *
     * CREATE TABLE addresses (
     *     address_id INT PRIMARY KEY,
     *     user_id INT FOREIGN KEY,
     *     type VARCHAR(20),  // HOME, OFFICE, BILLING
     *     street VARCHAR(100),
     *     city VARCHAR(50),
     *     zip VARCHAR(10)
     * );
     *
     * // Query:
     * SELECT u.*, a.* FROM users u
     * JOIN addresses a ON u.user_id = a.user_id
     * WHERE u.user_id = 123;
     *
     * Problem:
     * - Multiple queries/JOINs
     * - Slow for deeply nested data
     *
     * MongoDB approach:
     * {
     *     _id: ObjectId(),
     *     name: "John Doe",
     *     email: "john@example.com",
     *     addresses: [
     *         {
     *             type: "HOME",
     *             street: "123 Main St",
     *             city: "Mumbai",
     *             zip: "400001"
     *         },
     *         {
     *             type: "OFFICE",
     *             street: "456 Business Park",
     *             city: "Bangalore",
     *             zip: "560001"
     *         }
     *     ]
     * }
     *
     * // Query: Single find, no JOIN
     * db.users.findOne({ _id: user_id })
     *
     * Benefits:
     * ✅ All data in one document
     * ✅ No JOINs needed
     * ✅ Faster access
     * ✅ Easier for nested relationships
     */

    /**
     * ============= SCENARIO 3: Rapidly Changing Schema =============
     *
     * Problem with MySQL:
     * - New requirement: Add "referral_code" field to users
     * - Must ALTER TABLE (locks table)
     * - Can be slow for millions of rows
     *
     * MySQL:
     * ALTER TABLE users ADD COLUMN referral_code VARCHAR(20);
     * // Locks table → downtime → users can't access
     *
     * MongoDB:
     * - Simply add field to documents when needed
     * - No schema migration
     * - Zero downtime
     *
     * Code:
     * db.users.updateMany({}, { $set: { referral_code: generateCode() } })
     *
     * Benefits:
     * ✅ No downtime
     * ✅ Gradual rollout
     * ✅ Can have mixed schemas during transition
     */

    /**
     * ============= SCENARIO 4: Logging & Time-Series Data =============
     *
     * Problem with MySQL:
     * - Millions of log entries
     * - Constantly growing
     * - Hard to manage
     *
     * MongoDB approach:
     * db.logs.insertOne({
     *     timestamp: ISODate("2024-01-15T10:30:00Z"),
     *     level: "ERROR",
     *     service: "PaymentService",
     *     message: "Payment failed",
     *     stacktrace: "...",
     *     userId: "user-123"
     * })
     *
     * Benefits:
     * ✅ Easy to scale (horizontal sharding)
     * ✅ Can set TTL to auto-delete old logs
     * ✅ No schema constraints
     * ✅ Fast writes (important for logs)
     *
     * // Auto-delete logs older than 30 days
     * db.logs.createIndex({ "timestamp": 1 }, { expireAfterSeconds: 2592000 })
     */

    /**
     * ============= COMPREHENSIVE COMPARISON TABLE =============
     *
     * Feature              | MySQL            | MongoDB
     * ─────────────────────┼──────────────────┼──────────────────
     * Schema               | Fixed            | Flexible
     * Nested Data          | Requires JOINs   | Native support
     * NULL values          | Common           | Rare
     * Scaling              | Vertical         | Horizontal (sharding)
     * Transactions         | ACID             | ACID (recent)
     * Consistency          | Strong           | Eventual (configurable)
     * Storage              | Normalized       | Can be denormalized
     * Replication          | Master-Slave     | Replica sets
     * Query Language       | SQL              | MongoDB Query Language
     * ─────────────────────┼──────────────────┼──────────────────
     * Best For             | Structured data  | Flexible, nested data
     *                      | Relational       | Document-oriented
     *                      | ACID needed      | Horizontal scaling
     * ─────────────────────┴──────────────────┴──────────────────
     */

    /**
     * ============= WHEN TO USE EACH =============
     */

    /**
     * Use MySQL (Relational) when:
     * ✅ Data is highly structured
     * ✅ Strong relationships between tables
     * ✅ ACID transactions critical
     * ✅ Complex queries with many JOINs
     * ✅ Reporting and analytics
     *
     * Examples:
     * - Banking systems (transactions)
     * - Inventory management (relationships)
     * - Employee management (structured data)
     */

    /**
     * Use MongoDB (Document) when:
     * ✅ Data is semi-structured or unstructured
     * ✅ Schema changes frequently
     * ✅ Nested/hierarchical data
     * ✅ Horizontal scaling needed
     * ✅ High write throughput
     * ✅ Document-oriented access patterns
     *
     * Examples:
     * - User profiles (flexible attributes)
     * - Product catalogs (varying schema)
     * - Logs and events (high volume)
     * - Content management (nested content)
     * - IoT sensor data (varied formats)
     */

    /**
     * ============= REAL WORLD: Paytm-like Service =============
     *
     * Use MySQL for:
     * - Users table (strong consistency needed)
     * - Wallet table (ACID transactions critical)
     * - Settlements (financial records)
     *
     * Use MongoDB for:
     * - Orders (flexible schema)
     * - User profiles (varying attributes)
     * - Payment history (flexible logging)
     * - KYC documents (different formats)
     *
     * Result: Polyglot Persistence
     * ├─ MySQL: Financial (strict consistency)
     * ├─ MongoDB: Orders, Profiles (flexible)
     * ├─ Redis: Cache (fast access)
     * └─ Elasticsearch: Logs, Search (analytics)
     */
}

/*
 * KEY TAKEAWAY 🎯
 *
 * MongoDB is NOT "better" than MySQL in general.
 *
 * MongoDB is BETTER for:
 * - Flexible, evolving schemas
 * - Deeply nested data
 * - Document-oriented queries
 * - Horizontal scaling
 * - High write throughput
 *
 * MySQL is BETTER for:
 * - Structured, relational data
 * - Complex multi-table queries
 * - ACID transactions
 * - Data integrity constraints
 * - Reporting/analytics
 *
 * Best Practice: Use both! (Polyglot Persistence)
 * - Choose the right tool for each use case
 * - MongoDB for flexibility
 * - MySQL for consistency
 */

