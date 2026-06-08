package com.example.ticket_booking_system.entities;

import com.example.ticket_booking_system.StatusSeat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value=1,message = "Seat number cannot be blank")
    private int seatNumber;

    @Enumerated(EnumType.STRING)
    private StatusSeat status;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Version
    private int version;
}
