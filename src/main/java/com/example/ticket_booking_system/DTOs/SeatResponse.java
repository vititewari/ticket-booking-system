package com.example.ticket_booking_system.DTOs;

import com.example.ticket_booking_system.StatusSeat;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeatResponse {
    @Min(value=1,message = "Seat number cannot be blank")
    private int seatNumber;

    private StatusSeat status;

    private Long eventId;
    private Long seatId;
}
