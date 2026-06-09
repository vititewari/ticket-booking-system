package com.example.ticket_booking_system.events;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {
    @EventListener
    @Async
    public void handleBookingConfirmed(BookingConfirmedEvent event) {
        System.out.println("Sending confirmation for booking " +
                event.getBookingId() + " to " + event.getUserName());
    }

    @EventListener
    @Async
    public void handleBookingCancelled(BookingCancelledEvent event) {
        System.out.println("Sending cancellation for booking " +
                event.getBookingId() + " to " + event.getUserName());
    }

    @EventListener
    @Async
    public void handleBookingExpired(BookingExpiredEvent event) {
        System.out.println("Sending expiry notification for booking " +
                event.getBookingId() + " to " + event.getUserName());
    }
}
