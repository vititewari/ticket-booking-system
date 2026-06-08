package com.example.ticket_booking_system.DTOs;

import com.example.ticket_booking_system.StatusBooking;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class AuditResponse {

    private Long bookingId;

    @Enumerated(EnumType.STRING)
    private StatusBooking previousStatus;

    @Enumerated(EnumType.STRING)
    private StatusBooking nextStatus;

    private LocalDateTime changedAt;
    private String changedBy;
}
