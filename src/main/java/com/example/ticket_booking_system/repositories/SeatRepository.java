package com.example.ticket_booking_system.repositories;

import com.example.ticket_booking_system.StatusSeat;
import com.example.ticket_booking_system.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,Long> {
    List<Seat> findByEventId(Long eventId);
}
