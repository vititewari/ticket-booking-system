package com.example.ticket_booking_system.services;

import com.example.ticket_booking_system.DTOs.EventRequest;
import com.example.ticket_booking_system.DTOs.EventResponse;
import com.example.ticket_booking_system.DTOs.EventSummaryDTO;
import com.example.ticket_booking_system.DTOs.SeatResponse;
import com.example.ticket_booking_system.StatusSeat;
import com.example.ticket_booking_system.entities.Event;
import com.example.ticket_booking_system.entities.Seat;
import com.example.ticket_booking_system.exceptions.EventNotFoundException;
import com.example.ticket_booking_system.repositories.EventRepository;
import com.example.ticket_booking_system.repositories.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public EventResponse getResponse(@NonNull Event event){
        return new EventResponse(event.getId(), event.getName(), event.getDate(), event.getVenue(), event.getTotalSeats());
    }

    @Transactional
    public EventResponse addEvent(@NonNull EventRequest request){
        Event event = new Event();
        event.setDate(request.getDate());
        event.setName(request.getName());
        event.setTotalSeats(request.getTotalSeats());
        event.setDurationInMinutes(request.getDurationInMinutes());
        event.setVenue(request.getVenue());
        Event saved = eventRepository.save(event);

        // Auto-create seats
        for (int i = 1; i <= request.getTotalSeats(); i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(i);
            seat.setStatus(StatusSeat.AVAILABLE);
            seat.setEvent(saved);
            seatRepository.save(seat);
        }

        return getResponse(saved);
    }
    public  EventResponse getEventById(Long id){
        Event event = eventRepository.findById(id)
                .orElseThrow(()-> new EventNotFoundException(id));
        return getResponse(event);
    }

    public List<EventResponse> getAllEvents(){
        List<Event> events=eventRepository.findAll();
        return events.stream()
                .map(event->getResponse(event))
                .collect(Collectors.toList());
    }

    public EventResponse updateEvent(Long eventId, EventRequest request){
        Event event=eventRepository.findById(eventId)
                .orElseThrow(()->new EventNotFoundException(eventId));
        event.setVenue(request.getVenue());
        event.setDate(request.getDate());
        event.setName(request.getName());
        event.setTotalSeats(request.getTotalSeats());
        event.setDurationInMinutes(request.getDurationInMinutes());
        Event saved = eventRepository.save(event);
        return getResponse(saved);
    }

    public void deleteEvent(Long id){
        //add rule later that cannot delete event if shows exist
        eventRepository.deleteById(id);
    }

    public List<EventResponse> searchEvents(String name){
        return eventRepository.findByNameContaining(name)
                .stream()
                .map(event->getResponse(event))
                .collect(Collectors.toList());
    }

    public List<EventResponse> searchUpcomingEvents(LocalDate date){
        return eventRepository.findByDateAfter(date)
                .stream()
                .map(event->getResponse(event))
                .collect(Collectors.toList());
    }

    public Map<String, List<SeatResponse>> getSeatMap(Long id) {
        return seatRepository.findByEventId(id)
                .stream()
                .map(seat -> new SeatResponse(seat.getSeatNumber(), seat.getStatus(), seat.getEvent().getId(), seat.getId()))
                .collect(Collectors.groupingBy(seat -> seat.getStatus().name()));
    }

    public EventSummaryDTO getSummary(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(()->new EventNotFoundException(id));
        EventSummaryDTO summary = new EventSummaryDTO();
        List<Seat> seats = seatRepository.findByEventId(id);
        long available = seats.stream()
                .filter(seat -> seat.getStatus() == StatusSeat.AVAILABLE)
                .count();
        long held = seats.stream()
                .filter(seat -> seat.getStatus() == StatusSeat.HELD)
                .count();
        long booked = seats.stream()
                .filter(seat -> seat.getStatus() == StatusSeat.BOOKED)
                .count();
        float occupancy = ((float)(held + booked) / event.getTotalSeats()) * 100;
        String occupanyRate = occupancy +"%";

        summary.setTotalSeats(event.getTotalSeats());
        summary.setBookedSeats(booked);
        summary.setAvailableSeats(available);
        summary.setHeldSeats(held);
        summary.setOccupancyRate(occupanyRate);

        return summary;
    }
}
