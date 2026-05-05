# Wallettrial_2

![Java](https://img.shields.io/badge/Java-Console_App-orange)
![Build](https://img.shields.io/badge/Build-Ant-blue)
![License](https://img.shields.io/badge/License-MIT-green)

A Java console application that simulates a simple digital wallet system with user registration, login, deposits, withdrawals, transfers, and transaction history tracking.

## Overview

`Wallettrial_2` is a file-based wallet management project built with Java and the NetBeans/Ant project structure. It was created as a lightweight academic-style project for practicing object-oriented design, file handling, and menu-driven application flow.

## Features

- User registration and login
- Persistent account storage using local text files
- Deposit flow with bank account validation
- Withdrawal flow with wallet booth validation
- Peer-to-peer balance transfer between users
- Transaction history per account
- Simple admin view for listing registered accounts

## Tech Stack

- Java
- Apache Ant
- NetBeans project structure
- Plain text file storage

## Project Structure

```text
.
|-- src/wallettrial_2/
|   |-- App.java
|   |-- Account.java
|   |-- User.java
|   |-- FileUtil.java
|   |-- BankUtil.java
|   |-- BoothUtil.java
|   |-- ...
|-- test/
|-- build.xml
|-- manifest.mf
|-- account.txt
|-- bank.txt
|-- transactions.txt
`-- WalletBooth.txt
```

## How It Works

The application starts with a menu that lets a user:

1. Register a new account
2. Log in to an existing account
3. Exit the application

After login, a user can:

1. Deposit money after validating a bank account ID and PIN
2. Withdraw money through a valid wallet booth
3. Transfer money to another registered user
4. Check current balance
5. View transaction history
6. Log out

There is also a basic admin shortcut in the current implementation:

- Username: `admin`
- Password: `admin`

This opens a view of all registered accounts and balances.

## Getting Started

### Prerequisites

- JDK 8 or later
- Apache Ant

### Run With Ant

```bash
ant clean
ant run
```

### Run Manually

```bash
javac -d build/classes src/wallettrial_2/*.java
java -cp build/classes wallettrial_2.App
```

## Data Files

The application stores data in plain text files in the project root:

- `account.txt` stores usernames, passwords, and balances
- `transactions.txt` stores transaction history
- `bank.txt` stores bank account information used for deposit validation
- `WalletBooth.txt` stores valid booth numbers used for withdrawal validation

## Current Limitations

- Passwords are stored in plain text
- Data storage uses local files instead of a database
- Input validation is minimal for malformed numeric input
- No automated test suite is currently configured
- The admin credentials are hardcoded for demonstration purposes

## Ideas For Future Improvements

- Hash and salt user passwords
- Replace text files with a relational database
- Add exception-safe input handling
- Add unit and integration tests
- Introduce account locking and audit logging
- Build a GUI or web frontend

## Contributing

Contributions, cleanup, and educational improvements are welcome. Please read [CONTRIBUTING.md](CONTRIBUTING.md) before opening a pull request.

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
