package com.example.ticket_booking_system.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String venue;
    @NotNull
    private LocalDate date;
    @Min(value=1)
    private int totalSeats;
    @Min(value=1)
    private int durationInMinutes;
}
