package com.example.ticket_booking_system.controllers;

import com.example.ticket_booking_system.DTOs.AuditResponse;
import com.example.ticket_booking_system.DTOs.BookingRequest;
import com.example.ticket_booking_system.DTOs.BookingResponse;
import com.example.ticket_booking_system.entities.BookingAudit;
import com.example.ticket_booking_system.services.BookingService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@AllArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{id}")
    public BookingResponse getBookingById(@PathVariable Long id){
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public List<BookingResponse> addBooking(@Valid @RequestBody BookingRequest request){
        return bookingService.addBooking(request);
    }

    @PutMapping("/{id}/confirm")
    public BookingResponse confirmBooking(@PathVariable Long id){
        return bookingService.confirmBooking(id);
    }

    @PutMapping("/{id}/cancel")
    public  BookingResponse cancelBooking(@PathVariable Long id){
        return bookingService.cancelBooking(id);
    }

    @GetMapping("/{id}/audit")
    public List<AuditResponse> getAuditsOfBookingById(@PathVariable Long id){
        return bookingService.getAuditTrail(id);
    }
}
