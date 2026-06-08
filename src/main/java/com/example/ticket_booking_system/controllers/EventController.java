package com.example.ticket_booking_system.controllers;

import com.example.ticket_booking_system.DTOs.EventRequest;
import com.example.ticket_booking_system.DTOs.EventResponse;
import com.example.ticket_booking_system.DTOs.EventSummaryDTO;
import com.example.ticket_booking_system.DTOs.SeatResponse;
import com.example.ticket_booking_system.services.EventService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequestMapping("/events")
@RestController
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService){
        this.eventService=eventService;
    }

    @GetMapping
    public List<EventResponse> getAllEvents(){
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable Long id){
        return eventService.getEventById(id);
    }

    @PostMapping
    public EventResponse addEvent(@Valid @RequestBody EventRequest request){
        return eventService.addEvent(request);
    }

    @PutMapping("{id}")
    public EventResponse updateEvent(@PathVariable Long id, @Valid @RequestBody EventRequest request){
        return eventService.updateEvent(id,request);
    }

    @DeleteMapping("{id}")
    public void deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
    }

    @GetMapping("/search")
    public List<EventResponse> searchByName(@RequestParam String name){
        return eventService.searchEvents(name);
    }

    @GetMapping("/upcoming")
    public List<EventResponse> searchUpcomingEvents(@RequestParam LocalDate date){
        return eventService.searchUpcomingEvents(date);
    }
    @GetMapping("/{id}/seatmap")
    public Map<String, List<SeatResponse>> getSeatMap(@PathVariable Long id) {
        return eventService.getSeatMap(id);
    }

    @GetMapping("/{id}/summary")
    public EventSummaryDTO getSummary(@PathVariable Long id){
        return eventService.getSummary(id);
    }
}
