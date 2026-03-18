# BeerNotes v2

A full-stack web application for beer enthusiasts to track and rate their favorite beers. Built with a Java Spring Boot backend providing RESTful APIs and a React frontend for an intuitive user interface.

## Features

- **CRUD Operations**: Create, read, update, and delete beer entries.
- **Beer Tracking**: Store details like name, style, brewery, origin, and personal notes.
- **Rating System**: Rate beers and keep personal notes.
- **RESTful API**: Well-documented API endpoints for seamless integration.
- **Database**: In-memory H2 database for development and testing.
- **Swagger UI**: Interactive API documentation.

## Tech Stack

- **Backend**: Java 21, Spring Boot 3.5.8, Spring Data JPA, H2 Database
- **Frontend**: React (separate repository/project)
- **Testing**: JUnit, Mockito
- **Documentation**: OpenAPI/Swagger

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- Node.js and npm (for frontend development)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/beerNotes-v2.git
   cd beerNotes-v2
   ```

2. Build the backend:
   ```bash
   mvn clean install
   ```

## Running the Application

### Backend (Spring Boot)

Start the Spring Boot application:
```bash
mvn spring-boot:run
```

The backend will be available at:
- **Application**: http://localhost:8080/beernotes
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **H2 Console**: http://localhost:8080/h2-console/

### Frontend (React)

Navigate to your React frontend project directory and run:
```bash
npm start
```

The frontend will be available at: http://localhost:3000

## API Endpoints

- `GET /api/beernotes/beerlist` - Retrieve all beers
- `POST /api/beernotes/saveBeer` - Save a new beer
- `DELETE /api/beernotes/deleteBeer/{beerName}` - Delete a beer by name
- `PUT /api/beernotes/updateBeerNote/{beerName}` - Update beer notes

For detailed API documentation, visit the Swagger UI.

## Database

The application uses an in-memory H2 database for development. Access the H2 console at http://localhost:8080/h2-console/ with:
- **JDBC URL**: jdbc:h2:mem:testdb
- **Username**: sa
- **Password**: password

## Testing

Run the tests:
```bash
mvn test
```



# Reference Documentation

### Swagger UI Details
http://localhost:8080/swagger-ui/index.html

### H2 DB Details
http://localhost:8080/h2-console/

### BeerNotes Url (local)
http://localhost:8080/beernotes

### Frontend React Notes
npm start  - builds/runs react app

http://localhost:3000  - When running frontend/ webapp alone