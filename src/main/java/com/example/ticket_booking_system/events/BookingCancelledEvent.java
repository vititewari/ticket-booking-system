package com.example.ticket_booking_system.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingCancelledEvent {
    private final Long bookingId;
    private final String userName;
}
