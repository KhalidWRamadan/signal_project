# Week 3 Project Assignment Deliverables

This document serves as the formal submission for the testing and code coverage requirements of the Cardiovascular Health Monitoring System (CHMS) Project Part 3.

## 1. Unit Test Verification

Comprehensive data-driven test cases were implemented for all missing functionalities using JUnit 5. The test suite covers normal operations and edge cases (e.g., verifying boundary conditions, rapid saturation drops within sliding time windows, and invalid file string parsing).

The screenshot below verifies the successful execution of `mvn clean test`, showing all 20 implemented tests passing without errors or failures.

![Unit Test Execution](Screenshot%202026-05-02%20at%2017.51.12.png)

## 2. Code Coverage Documentation

The JaCoCo Maven plugin was utilized to generate a code coverage report for the CHMS. 

The screenshot below displays the coverage overview across the project packages:

![JaCoCo Code Coverage Report](Screenshot%202026-05-02%20at%2017.57.52.png)

### Coverage Explanation & Un-tested Code Rationale

The newly implemented core logic packages achieved excellent test coverage:
- `com.alerts`: **100% coverage**. The `AlertGenerator` correctly delegates to the modular strategies.
- `com.alerts.strategies`: **97% instruction / 85% branch coverage**. All 5 alert rules (Trend, Critical Threshold, Rapid Drop, Hypotensive Hypoxemia, ECG sliding window, and Manual) are exhaustively tested.
- `com.data_management`: **79% instruction / 73% branch coverage**. `FileDataReader`, `DataStorage`, and `Patient` fetching logic are fully verified.

**Un-tested Code:**
The red bars showing 0% coverage belong strictly to pre-provided mock components and external I/O routers that were intentionally left out of the unit testing scope for this assignment:
- `com.cardio_generator.generators`: Contains the mock patient vitals generators.
- `com.cardio_generator.outputs`: Contains the networking and file I/O classes (TCP, WebSocket).
- `com`: Contains the `Main.java` router class used merely for command-line argument dispatching. 

Because these components simulate external hardware or handle pure I/O networking logic, unit testing them was not required by the week 3 rubric.