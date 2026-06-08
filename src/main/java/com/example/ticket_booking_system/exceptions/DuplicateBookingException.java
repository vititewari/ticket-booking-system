package com.example.ticket_booking_system.exceptions;
public class DuplicateBookingException extends RuntimeException {
    public DuplicateBookingException() {
        super("Duplicate seat IDs in request");
    }
}