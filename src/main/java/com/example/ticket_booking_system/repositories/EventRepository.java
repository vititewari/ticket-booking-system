package com.example.ticket_booking_system.repositories;

import com.example.ticket_booking_system.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event,Long> {
    List<Event> findByNameContaining(String name);
    List<Event> findByDateAfter(LocalDate date);
}
