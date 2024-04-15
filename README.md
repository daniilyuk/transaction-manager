# Bank Microservice Transaction Manager

Bank Microservice Transaction Manager is a service for managing transactions, limits, and currency exchange rates.

## Components

1. **Database**: PostgreSQL database for storing transaction, limit, and currency rate information.
2. **Services**:
   - Transaction Service: Processes transactions and manages their limits.
   - Limit Service: Manages limits for transactions.
   - Currency Service: Retrieves and manages currency exchange rates.
3. **API**:
   - RESTful API for interacting with the services.

## Getting Started

### Prerequisites

- Java 17 or later
- Maven
- PostgreSQL 15

### Installation

1. Clone the repository: `git clone https://github.com/daniilyuk/transaction-manager.git`
2. Navigate to the project directory: `cd transaction-manager`
3. Install dependencies: `mvn install`

## Usage

1. Run the application: `mvn spring-boot:run`
2. Access the API endpoints at [http://localhost:8080](http://localhost:8080)

## Docker Setup

### Prerequisites

- Docker

### Installation

1. Clone the repository: `git clone https://github.com/daniilyuk/transaction-manager.git`
2. Navigate to the project directory: `cd transaction-manager`

### Usage

1. Install Docker on your system if it's not already installed.
2. Run the following commands to build and run the container:

docker-compose up --build

## Documentation

API documentation is provided using Swagger. Once the application is running, you can access the Swagger UI at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) to view and test the available endpoints.


