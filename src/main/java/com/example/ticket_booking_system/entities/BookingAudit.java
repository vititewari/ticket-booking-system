package com.example.ticket_booking_system.entities;

import com.example.ticket_booking_system.StatusBooking;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Enumerated(EnumType.STRING)
    private StatusBooking previousStatus;

    @Enumerated(EnumType.STRING)
    private StatusBooking nextStatus;

    private LocalDateTime changedAt;
    private String changedBy;
}
