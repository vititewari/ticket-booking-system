package com.example.ticket_booking_system.controllers;

import com.example.ticket_booking_system.DTOs.SeatRequest;
import com.example.ticket_booking_system.DTOs.SeatResponse;
import com.example.ticket_booking_system.entities.Event;
import com.example.ticket_booking_system.services.SeatService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
@AllArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("{id}")
    public SeatResponse getSeatById(@PathVariable Long id){
        return seatService.getSeatById(id);
    }

    @GetMapping
    public List<SeatResponse> getAllAvailableSeats(){
        return seatService.getAllAvailableSeats();
    }

    @PostMapping
    public SeatResponse addSeat(@Valid @RequestBody SeatRequest request){
        return seatService.addSeat(request);
    }

    @PutMapping("{id}")
    public SeatResponse updateSeat(@PathVariable Long id, @Valid @RequestBody SeatRequest request){
        return seatService.updateSeat(id,request);
    }

    @DeleteMapping("{id}")
    public void deleteSeat(@PathVariable Long id){
        seatService.deleteSeat(id);
    }

    @GetMapping("/events")
    public List<SeatResponse> getSeatsByEvent(@RequestParam Long eventId){
        return seatService.getSeatsByEvent(eventId);
    }
}
