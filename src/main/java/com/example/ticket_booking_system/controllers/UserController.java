package com.example.ticket_booking_system.controllers;

import com.example.ticket_booking_system.DTOs.UserRequest;
import com.example.ticket_booking_system.DTOs.UserResponse;
import com.example.ticket_booking_system.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse addUser(@RequestBody UserRequest request){
        return userService.addUser(request);
    }

    @GetMapping("{id}")
    public UserResponse getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @PutMapping("/update")
    public UserResponse updateUser(@RequestParam Long id, @Valid @RequestBody UserRequest request){
        return userService.updateUser(id,request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/search")
    public List<UserResponse> findUserByName(@RequestParam String name){
        return userService.findUserByName(name);
    }
    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
