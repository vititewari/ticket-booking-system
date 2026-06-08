package com.example.ticket_booking_system.exceptions;

public class BookingNotPendingException extends RuntimeException {
    public BookingNotPendingException(Long id) {
        super("Booking not pending with id: "+id);
    }
}
