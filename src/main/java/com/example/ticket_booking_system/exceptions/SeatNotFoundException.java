package com.example.ticket_booking_system.exceptions;

public class SeatNotFoundException extends RuntimeException {
    public SeatNotFoundException(Long id) {
        super("Seat not found with id: "+id);
    }
}
