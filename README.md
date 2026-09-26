# Robust ATM Transaction System (OOAD & BCE Architecture)

An Object-Oriented Analysis and Design (OOAD) software project developed at Eskişehir Technical University for the **Principles of Software Design** course.

---

## Overview
This project simulates a secure and modular ATM transaction workflow communicating with a centralized banking authority. Developed with a strict Object-Oriented Analysis & Design methodology, the system maps requirements through formal Textual Analysis (Noun/Verb analysis) and encapsulates core banking operations via the **Boundary-Controller-Entity (BCE)** architectural pattern.

---

## Architecture & Design Patterns

### Boundary-Controller-Entity (BCE) Mapping
* **Boundary:** 
  * `ATMUI`: Command-line interface providing interactive user prompts, formatted menus, and console animations.
  * `BankCentralSystem`: Boundary interface simulating the core banking authorization server and central database.
* **Controller:** 
  * `ATMController`: Orchestrates application state, session lifecycle, security/retry constraints, and monetary transaction dispatching.
* **Entity:** 
  * `CustomerAccount`: Encapsulates immutable account credentials (card number, PIN) and mutable balance states.

### Design Patterns & Core Mechanisms
* **Singleton Pattern:** Implemented in both `BankCentralSystem` and `ATMController` to ensure synchronized single-point access to the central bank service and local ATM terminal controller.
* **Security & Lockout Flow:** Enforces a maximum of 3 failed PIN attempts before system termination/card retention.
* **Audit Trail / Logging:** Formats and appends completed monetary transactions (type, amount, timestamp, card ID) directly to the central ledger.

---

## Repository Structure
```text
ooad-atm-transaction-system/
├── .gitignore
├── pom.xml
├── README.md
├── docs/
│   ├── atm-system-design-specification.pdf
│   └── models/
│       ├── class.mdj
│       └── usecase.mdj
└── src/
    ├── main/
    │   └── java/
    │       └── atm/
    │           └── estu/
    │               └── aykutefefatih/
    │                   ├── ATMController.java
    │                   ├── ATMUI.java
    │                   ├── BankCentralSystem.java
    │                   └── CustomerAccount.java
    └── test/
        └── java/
            └── atm/
                └── estu/
                    └── aykutefefatih/
                        └── ATMControllerTEST.java
```

---

## Documentation & Models
Official engineering deliverables and architectural diagrams are located in the [`docs/`](docs/) directory:
* [`docs/atm-system-design-specification.pdf`](docs/atm-system-design-specification.pdf): Detailed project specification including Vision Statement, Use Case definitions, Textual Analysis tables, UML Class diagrams, and verification logs.
* [`docs/models/`](docs/models/): StarUML model assets (`class.mdj`, `usecase.mdj`).

---

## Verification & Testing
The system includes a 10-stage sequential JUnit 5 test suite (`ATMControllerTEST.java`) verifying edge cases, session lifecycles, and transaction integrity:
* **Card & PIN Authentication:** Validates invalid card lookup, wrong PIN rejections, and valid credential approvals.
* **Security Constraints:** Verifies the 3-consecutive-failed-attempts security lockout scenario.
* **Monetary Operations:** Verifies balance checks, deposit increments, and withdrawal balance boundaries (insufficient funds checks).
* **Audit Verification:** Verifies formatted timestamped log records for completed transactions.

---

## Team & Contributors
* **Efe Kemal Yılmaz**
* **Aykut Cıncık**
* **Fatih Furkan Şahin**

---

## Build & Execution

### Prerequisites
* JDK 17 or higher
* Apache Maven

### Running Unit Tests
Execute the JUnit test suite directly via Maven:
```bash
mvn clean test
```

### Packaging and Running the Application
1. Package the project into an executable JAR:
   ```bash
   mvn clean package
   ```
2. Run the console ATM application:
   ```bash
   java -jar target/ATM_TRANSACTION_SYSTEM-1.0.jar
   ```