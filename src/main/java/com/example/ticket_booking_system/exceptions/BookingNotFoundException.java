package com.example.ticket_booking_system.exceptions;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(Long id) {
        super("Booking not found with id: "+id);
    }
}
