package com.example.ticket_booking_system.exceptions;

public class SeatUnavailableException extends RuntimeException {
    public SeatUnavailableException(Long id) {
        super("seat not available with id: "+id);
    }
}
