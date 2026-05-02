# CHMS UML System Design Documentation. Student ID : i6447459

This document outlines the architectural rationale and design choices for the four key subsystems of the Cardiovascular Health Monitoring System (CHMS).

## 1. Alert Generation System

The Alert Generation System is designed with a strong emphasis on the Separation of Concerns principle. The central `AlertGenerator` class acts as a coordinator, delegating specific tasks to specialized components. To handle the requirement that patients may have different, personalized alert rules, the `ThresholdRepository` and `PatientThresholdProfile` classes were introduced. This allows alert rules (`AlertRule`) to be dynamically loaded and modified per patient without altering the core evaluation logic.

When a threshold is breached, an `Alert` entity is created. Rather than having the generator handle notifications, the `AlertManager` is responsible for routing these alerts to the appropriate `MedicalStaff`. This modular design ensures that if the hospital decides to change how staff are notified (e.g., adding SMS or pager endpoints), only the `AlertManager` needs to be updated. Overall, this architecture provides a highly extensible and maintainable blueprint for real-time critical condition detection.

## 2. Data Storage System

The Data Storage System prioritizes security, privacy, and data lifecycle management. By defining a core `DataStorage` interface, the system remains flexible; the concrete `SecureDataStorage` implementation can be swapped out if the underlying database technology changes. To address the "Security & Privacy Matter" requirement, an `AccessController` acts as a gatekeeper, verifying that only authorized `MedicalStaff` can retrieve sensitive `PatientData` via the `DataRetriever`. This separation ensures that read queries and write operations are securely decoupled.

Furthermore, because cardiovascular data streams are continuous and high-volume, a dedicated `StoragePolicy` class was introduced. This class isolates the responsibility of data retention and deletion policies (e.g., removing records older than X days), keeping the main storage classes free of maintenance logic. This modularity ensures compliance with hospital data lifecycle regulations while maintaining high performance for real-time monitoring.

## 3. Patient Identification System

The Patient Identification System is structured to ensure absolute data integrity when mapping continuous simulator streams to actual hospital records. The `IdentityManager` serves as the orchestration layer, utilizing the `PatientIdentifier` to perform the actual matching logic against the `HospitalDatabase`.

Recognizing that simulator data may sometimes contain invalid or unmatched patient IDs, a dedicated `MismatchHandler` was incorporated into the design. Instead of failing silently or crashing the data pipeline, the `MismatchHandler` queues unresolved `PatientData` and flags anomalies for manual review. This guarantees that no incoming vitals are lost due to a mismatch, and it isolates error-handling logic from the happy-path identification flow. By encapsulating patient details within the `HospitalPatient` class, the system also ensures that sensitive medical history is only exposed when a valid, secure match is confirmed.

## 4. Data Access Layer

The Data Access Layer is designed around the Strategy and Adapter patterns to isolate the CHMS from the specifics of incoming data protocols. A generic `DataListener` interface was established, with specialized subclasses (`TCPDataListener`, `WebSocketDataListener`, and `FileDataListener`) handling the unique connection and streaming logic for their respective protocols. This satisfies the requirement to easily swap in new data sources without rewriting the core application.

Once raw data is received, it is handed off to the `DataSourceAdapter`. The adapter uses a `DataParser` to convert raw, unstructured text (like CSV or JSON) into standardized `PatientData` objects. By decoupling the listening, parsing, and routing responsibilities, the system remains highly adaptable. If a new data format or transmission protocol is introduced in the future, developers only need to implement a new listener or parser class, leaving the rest of the CHMS completely unaware of the external changes.
