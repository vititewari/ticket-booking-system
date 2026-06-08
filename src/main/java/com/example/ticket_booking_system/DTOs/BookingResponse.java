package com.example.ticket_booking_system.DTOs;

import com.example.ticket_booking_system.StatusBooking;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private LocalDateTime createdAt;
    private Long userId;
    private List<Long> seatIds;
    private StatusBooking status;
}
