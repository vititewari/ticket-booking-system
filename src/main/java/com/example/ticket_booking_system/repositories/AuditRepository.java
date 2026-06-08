package com.example.ticket_booking_system.repositories;

import com.example.ticket_booking_system.entities.BookingAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<BookingAudit, Long> {
    List<BookingAudit> findByBookingId(Long bookingId);
}
