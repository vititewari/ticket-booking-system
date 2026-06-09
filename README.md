# Ticket Booking System

A production-ready REST API for managing event ticket bookings, built with Spring Boot.

## Architecture
Client Request
↓
Spring Security (JWT Filter)
↓
Controller Layer
↓
Service Layer
↓              ↓
JPA         ApplicationEventPublisher
↓                    ↓
Database          @EventListener
(NotificationEventListener)

### Event Flow
Booking Confirmed
↓
BookingService.confirmBooking()
↓
publishEvent(BookingConfirmedEvent)
↓
NotificationEventListener.handleBookingConfirmed()
↓
Notification sent (async, background thread)

### Concurrency Protection
User A → Book Seat 5 (version=1) ✅ → version becomes 2
User B → Book Seat 5 (version=1) ❌ → OptimisticLockException → 409

### Seat Lifecycle
AVAILABLE → HELD (on booking) → BOOKED (on confirm)
HELD → AVAILABLE (on cancel or 10 min expiry)

## Features

- **Event Management** — Create events with automatic seat generation
- **Multi-seat Booking** — Book multiple seats in a single atomic transaction
- **Concurrency Protection** — Optimistic locking (`@Version`) prevents double booking
- **Auto-expiry** — Scheduled job releases held seats after 10 minutes
- **Audit Trail** — Every booking status change is recorded
- **Event-driven Notifications** — Spring Events for async, loosely coupled notifications
- **JWT Authentication** — Secure endpoints with token-based auth
- **API Documentation** — Swagger UI at `/swagger-ui/index.html`

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
  -d '{"username":"admin@test.com","password":"password123"}'

# Login — copy the token from response
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin@test.com","password":"password123"}'
```

### Access Swagger UI
http://localhost:8080/swagger-ui/index.html

## API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login, returns JWT token |

### Events
| Method | Endpoint | Description |
|---|---|---|
| POST | /events | Create event + auto-generate seats |
| GET | /events | Get all events |
| GET | /events/{id} | Get event by ID |
| GET | /events/{id}/seatmap | Seat availability map |
| GET | /events/{id}/summary | Occupancy statistics |

### Seats
| Method | Endpoint | Description |
|---|---|---|
| GET | /seats | Get all available seats |
| GET | /seats/{id} | Get seat by ID |
| GET | /seats/events?eventId= | Seats by event |

### Users
| Method | Endpoint | Description |
|---|---|---|
| POST | /users | Create user |
| GET | /users/{id} | Get user by ID |
| GET | /users/search?name= | Search by name |

### Bookings
| Method | Endpoint | Description |
|---|---|---|
| POST | /bookings | Book multiple seats |
| PUT | /bookings/{id}/confirm | Confirm booking |
| PUT | /bookings/{id}/cancel | Cancel booking |
| GET | /bookings/{id} | Get booking by ID |
| GET | /bookings/{id}/audit | Booking audit trail |

## Key Design Decisions

### Auto Seat Generation
When an event is created with `totalSeats=100`, the system automatically generates 100 seat records with `AVAILABLE` status. Handled atomically with `@Transactional`.

### Concurrency
Two users booking the same seat simultaneously is handled via JPA optimistic locking. The `@Version` field on `Seat` ensures only one transaction succeeds — the other receives a 409 Conflict.

### Seat Hold Expiry
When a booking is created, the seat moves to `HELD` status. A scheduled job runs every 60 seconds and releases seats held for more than 10 minutes back to `AVAILABLE`.

### Audit Trail
Every status transition (null→PENDING, PENDING→CONFIRMED etc.) is recorded in `BOOKING_AUDIT` with timestamp and actor.

### Event-Driven Notifications
Notifications are published as Spring Events, allowing loose coupling. Replacing with SQS/RabbitMQ requires only changing the publisher — no business logic changes.

## Running Tests
```bash
# Start the application first on port 8080
# Then in the ticket-booking-tests project:
mvn test
```

## Future Improvements
- Docker + Docker Compose
- AWS SQS replacing Spring Events
- MySQL for production database
- Microservices split (notification-service, audit-service)
- React frontend