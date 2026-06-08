package com.example.ticket_booking_system.services;

import com.example.ticket_booking_system.DTOs.SeatRequest;
import com.example.ticket_booking_system.DTOs.SeatResponse;
import com.example.ticket_booking_system.StatusSeat;
import com.example.ticket_booking_system.entities.Seat;
import com.example.ticket_booking_system.exceptions.SeatNotFoundException;
import com.example.ticket_booking_system.repositories.EventRepository;
import com.example.ticket_booking_system.repositories.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final EventRepository eventRepository;
    public SeatService(SeatRepository seatRepository, EventRepository eventRepository){
        this.seatRepository=seatRepository;
        this.eventRepository=eventRepository;
    }
    public SeatResponse getResponse(Seat seat){
        return new SeatResponse(seat.getSeatNumber(), seat.getStatus(), seat.getEvent().getId(), seat.getId());
    }

    public SeatResponse getSeatById(Long id){
        Seat seat= seatRepository.findById(id)
                .orElseThrow(()->new SeatNotFoundException(id));
        return getResponse(seat);
    }

    public SeatResponse addSeat(SeatRequest request){
        Seat seat = new Seat();
        seat.setSeatNumber(request.getSeatNumber());
        seat.setStatus(request.getStatus());
        seat.setEvent(eventRepository.findById(request.getEventId()).orElseThrow());
        Seat saved=seatRepository.save(seat);
        return getResponse(saved);
    }

    public SeatResponse updateSeat(Long seatId, SeatRequest request){
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(()->new SeatNotFoundException(seatId));
        seat.setEvent(eventRepository.findById(request.getEventId()).orElseThrow());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setStatus(request.getStatus());
        Seat saved=seatRepository.save(seat);
        return getResponse(saved);
    }

    public void deleteSeat(Long id){
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new SeatNotFoundException(id));

        seatRepository.delete(seat);
    }
    public List<SeatResponse> getAllAvailableSeats(){
        return seatRepository.findAll()
                .stream()
                .filter(seat->seat.getStatus()== StatusSeat.AVAILABLE)
                .map(this::getResponse)
                .collect(Collectors.toList());
    }

    public List<SeatResponse> getSeatsByEvent(Long eventId){
        return seatRepository.findByEventId(eventId)
                .stream()
                .map(this::getResponse)
                .collect(Collectors.toList());
    }
}
