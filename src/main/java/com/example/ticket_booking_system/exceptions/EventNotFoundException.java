package com.example.ticket_booking_system.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long eventId) {
        super("No event found with id: "+eventId);
    }
}
