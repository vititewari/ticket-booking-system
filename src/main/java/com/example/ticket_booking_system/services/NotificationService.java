package com.example.ticket_booking_system.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Async
    public void sendBookingConfirmation(Long bookingId, String userName) {
        // Simulate sending email/SMS
        System.out.println("Sending confirmation for booking " + bookingId + " to " + userName);
    }
    @Async
    public void sendBookingCancellation(Long bookingId, String userName) {
        System.out.println("Sending cancellation for booking " + bookingId + " to " + userName);
    }

    @Async
    public void sendBookingExpired(Long bookingId, String userName) {
        System.out.println("Sending expiry notification for booking " + bookingId + " to " + userName);
    }
}
