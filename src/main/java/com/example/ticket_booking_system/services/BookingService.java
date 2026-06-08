package com.example.ticket_booking_system.services;

import com.example.ticket_booking_system.DTOs.AuditResponse;
import com.example.ticket_booking_system.DTOs.BookingRequest;
import com.example.ticket_booking_system.DTOs.BookingResponse;
import com.example.ticket_booking_system.StatusBooking;
import com.example.ticket_booking_system.StatusSeat;
import com.example.ticket_booking_system.entities.Booking;
import com.example.ticket_booking_system.entities.BookingAudit;
import com.example.ticket_booking_system.entities.Seat;
import com.example.ticket_booking_system.entities.User;
import com.example.ticket_booking_system.exceptions.*;
import com.example.ticket_booking_system.repositories.AuditRepository;
import com.example.ticket_booking_system.repositories.BookingRepository;
import com.example.ticket_booking_system.repositories.SeatRepository;
import com.example.ticket_booking_system.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@Service
@AllArgsConstructor
public class BookingService {
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final AuditRepository auditRepository;
    private final NotificationService notificationService;

    public BookingResponse getResponse(Booking booking){
        return new BookingResponse(booking.getId(), booking.getCreatedAt(),booking.getUser().getId(),
                List.of(booking.getSeat().getId()),
                booking.getStatus());
    }

    @Transactional
    public List<BookingResponse> addBooking(BookingRequest request){
        //checking if inputs such as seats=[1,1,2] are there
        if (request.getSeatIds().size() != new HashSet<>(request.getSeatIds()).size()) {
            throw new DuplicateBookingException();
        }
        List<Long> seatIds = request.getSeatIds();
        List<Seat> seats = seatIds.stream()
                .map(seatId->seatRepository.findById(seatId)
                        .orElseThrow(()->new SeatNotFoundException(seatId)))
                .collect(Collectors.toList());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()->new UserNotFoundException(request.getUserId()));
        seats.forEach(seat -> {
            if (seat.getStatus() != StatusSeat.AVAILABLE) {
                throw new SeatUnavailableException(seat.getId());
            }
        });

        return seats.stream()
                .map(seat -> {
                    seat.setStatus(StatusSeat.HELD);
                    seatRepository.save(seat);
                    Booking booking = new Booking();
                    booking.setStatus(StatusBooking.PENDING);
                    booking.setUser(user);
                    booking.setSeat(seat);
                    booking.setCreatedAt(LocalDateTime.now());
                    Booking saved = bookingRepository.save(booking);
                    BookingAudit audit = new BookingAudit();
                    audit.setBooking(saved);
                    audit.setChangedAt(LocalDateTime.now());
                    audit.setPreviousStatus(null);
                    audit.setNextStatus(StatusBooking.PENDING);
                    audit.setChangedBy(booking.getUser().getName());
                    auditRepository.save(audit);
                    return getResponse(saved);
                })
                .collect(Collectors.toList());

    }

    @Transactional
    public BookingResponse confirmBooking(Long id){
        Booking booking=bookingRepository.findById(id)
                .orElseThrow(()-> new BookingNotFoundException(id));
        if(booking.getStatus()!=StatusBooking.PENDING){
            throw(new BookingNotPendingException(id));
        }
        else{
            notificationService.sendBookingConfirmation(booking.getId(), booking.getUser().getName());
            Seat seat = booking.getSeat();
            seat.setStatus(StatusSeat.BOOKED);
            seatRepository.save(seat);
            BookingAudit audit = new BookingAudit();
            audit.setBooking(booking);
            audit.setPreviousStatus(booking.getStatus());
            audit.setNextStatus(StatusBooking.CONFIRMED);
            audit.setChangedAt(LocalDateTime.now());
            audit.setChangedBy(booking.getUser().getName());
            auditRepository.save(audit);
            booking.setStatus(StatusBooking.CONFIRMED);
            Booking saved = bookingRepository.save(booking);
            return getResponse(saved);
        }
    }

    @Transactional
    public BookingResponse cancelBooking(Long id){
        Booking booking=bookingRepository.findById(id)
                .orElseThrow(()-> new BookingNotFoundException(id));
        Seat seat = booking.getSeat();
        seat.setStatus(StatusSeat.AVAILABLE);
        seatRepository.save(seat);

        BookingAudit audit = new BookingAudit();
        audit.setBooking(booking);
        audit.setPreviousStatus(booking.getStatus());
        audit.setNextStatus(StatusBooking.EXPIRED);
        audit.setChangedAt(LocalDateTime.now());
        audit.setChangedBy(booking.getUser().getName());
        auditRepository.save(audit);
        notificationService.sendBookingCancellation(booking.getId(), booking.getUser().getName());
        booking.setStatus(StatusBooking.EXPIRED);
        Booking saved = bookingRepository.save(booking);
        return getResponse(saved);
    }

    public BookingResponse getBookingById(Long id){
        return getResponse(bookingRepository.findById(id)
                .orElseThrow(()->new BookingNotFoundException(id)));
    }

    public List<AuditResponse> getAuditTrail(Long bookingId) {
        List<BookingAudit> audits=auditRepository.findByBookingId(bookingId);
        return audits.stream()
                .map(audit->new AuditResponse(audit.getBooking().getId(), audit.getPreviousStatus(), audit.getNextStatus(), audit.getChangedAt(), audit.getChangedBy()))
                .collect(Collectors.toList());
    }
}
