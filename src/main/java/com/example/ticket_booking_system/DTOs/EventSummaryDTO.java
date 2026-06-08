package com.example.ticket_booking_system.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSummaryDTO {
    private long totalSeats;
    private long availableSeats;
    private long heldSeats;
    private long bookedSeats;
    private String occupancyRate;
}
