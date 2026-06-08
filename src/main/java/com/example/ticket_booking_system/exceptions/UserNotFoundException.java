package com.example.ticket_booking_system.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: "+id);
    }
}
