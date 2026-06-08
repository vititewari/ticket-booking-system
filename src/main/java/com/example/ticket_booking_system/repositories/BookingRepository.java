package com.example.ticket_booking_system.repositories;

import com.example.ticket_booking_system.StatusBooking;
import com.example.ticket_booking_system.StatusSeat;
import com.example.ticket_booking_system.entities.Booking;
import com.example.ticket_booking_system.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findBookingByStatusAndCreatedAtBefore(StatusBooking status, LocalDateTime time);
    boolean existsBySeatIdAndStatusNot(Long seatId, StatusBooking status);
}
