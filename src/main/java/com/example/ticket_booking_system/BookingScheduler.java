package com.example.ticket_booking_system;

import com.example.ticket_booking_system.entities.Booking;
import com.example.ticket_booking_system.entities.BookingAudit;
import com.example.ticket_booking_system.events.BookingCancelledEvent;
import com.example.ticket_booking_system.events.BookingExpiredEvent;
import com.example.ticket_booking_system.repositories.AuditRepository;
import com.example.ticket_booking_system.repositories.BookingRepository;
import com.example.ticket_booking_system.repositories.SeatRepository;
import com.example.ticket_booking_system.services.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class BookingScheduler {
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final AuditRepository auditRepository;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredHolds(){
        LocalDateTime expiredTime = LocalDateTime.now().minusMinutes(10);
        List<Booking> expiredBookings = bookingRepository.findBookingByStatusAndCreatedAtBefore(StatusBooking.PENDING,expiredTime);
        for(Booking booking:expiredBookings){
            BookingAudit audit = new BookingAudit();
            audit.setBooking(booking);
            audit.setPreviousStatus(audit.getNextStatus());
            audit.setNextStatus(StatusBooking.EXPIRED);
            audit.setChangedAt(LocalDateTime.now());
            audit.setChangedBy("SYSTEM");
            auditRepository.save(audit);


            booking.getSeat().setStatus(StatusSeat.AVAILABLE);
            booking.setStatus(StatusBooking.EXPIRED);
            seatRepository.save(booking.getSeat());
            Booking saved = bookingRepository.save(booking);
            eventPublisher.publishEvent(new BookingExpiredEvent(saved.getId(), booking.getUser().getName()));

        }
    }
}
