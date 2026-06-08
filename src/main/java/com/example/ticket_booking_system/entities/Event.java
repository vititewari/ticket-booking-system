package com.example.ticket_booking_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="event")
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name field cannot be blank")
    private String name;

    @NotBlank(message = "Venue field cannot be blank")
    private String venue;

    private LocalDate date;
    private int totalSeats;
    private int durationInMinutes;

    public Event(){}
    public Event(String name, String venue, LocalDate date, int totalSeats, int durationInMinutes){
        this.name=name;
        this.totalSeats=totalSeats;
        this.date=date;
        this.venue=venue;
        this.durationInMinutes=durationInMinutes;
    }
}
