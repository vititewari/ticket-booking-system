package com.example.ticket_booking_system.DTOs;

import com.example.ticket_booking_system.StatusSeat;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatRequest {
    @Min(value=1,message = "Seat number cannot be blank")
    private int seatNumber;

    private StatusSeat status;

    private Long eventId;
}
