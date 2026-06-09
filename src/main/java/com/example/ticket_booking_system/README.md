# Ticket Booking System

A production-ready REST API for managing event ticket bookings, built with Spring Boot.

## Features

- **Event Management** — Create and manage events with seat inventory
- **Multi-seat Booking** — Book multiple seats in a single atomic transaction
- **Concurrency Protection** — Optimistic locking (`@Version`) prevents double booking
- **Auto-expiry** — Scheduled job releases held seats after 10 minutes
- **Audit Trail** — Every booking status change is recorded
- **Async Notifications** — Background notifications on booking status changes
- **JWT Authentication** — Secure endpoints with token-based auth
- **API Documentation** — Swagger UI available at `/swagger-ui/index.html`

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA
- H2 Database (MySQL ready)
- Lombok
- REST Assured (API tests)
- Maven

## Getting Started

### Prerequisites
- Java 17+
- Maven

### Run the application
```bash
git clone https://github.com/vititewari/ticket-booking-system.git
cd ticket-booking-system
mvn spring-boot:run
```

### Register and Login
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user@test.com","password":"password123"}'

# Login — copy the token from response
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user@test.com","password":"password123"}'
```

## API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login, returns JWT token |

### Events
| Method | Endpoint | Description |
|---|---|---|
| POST | /events | Create event (ADMIN) |
| GET | /events | Get all events |
| GET | /events/{id} | Get event by ID |
| GET | /events/{id}/seatmap | Get seat availability map |
| GET | /events/{id}/summary | Get occupancy statistics |

### Bookings
| Method | Endpoint | Description |
|---|---|---|
| POST | /bookings | Book multiple seats |
| PUT | /bookings/{id}/confirm | Confirm booking |
| PUT | /bookings/{id}/cancel | Cancel booking |
| GET | /bookings/{id}/audit | View booking audit trail |

## Key Design Decisions

### Concurrency
Two users booking the same seat simultaneously is handled via JPA optimistic locking. The `@Version` field on `Seat` ensures only one transaction succeeds — the other receives a 409 Conflict.

### Seat Hold Expiry
When a booking is created, the seat moves to `HELD` status. A scheduled job runs every 60 seconds and releases any seats held for more than 10 minutes back to `AVAILABLE`.

### Auto Seat Generation
When an event is created with `totalSeats=100`, the system automatically generates 100 seat records with `AVAILABLE` status. This is handled atomically — if seat creation fails, the event creation rolls back (`@Transactional`).

### Audit Trail
Every status transition (null→PENDING, PENDING→CONFIRMED etc.) is recorded in `BOOKING_AUDIT` with timestamp and actor.

## Running Tests
```bash
# Start the application first, then in ticket-booking-tests project:
mvn test
```

## Future Improvements
- Docker + Docker Compose
- AWS SQS for async messaging
- MySQL for production database
- Microservices split
- React frontend