package com.example.ticket_booking_system.DTOs;

import com.example.ticket_booking_system.StatusBooking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookingRequest {
    private Long userId;
    private List<Long> seatIds;
}
