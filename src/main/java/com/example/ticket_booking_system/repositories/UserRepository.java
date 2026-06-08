package com.example.ticket_booking_system.repositories;

import com.example.ticket_booking_system.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findUserByNameContaining(String name);
}
