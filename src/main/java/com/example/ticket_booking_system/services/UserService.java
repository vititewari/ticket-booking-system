package com.example.ticket_booking_system.services;

import com.example.ticket_booking_system.DTOs.UserRequest;
import com.example.ticket_booking_system.DTOs.UserResponse;
import com.example.ticket_booking_system.entities.User;
import com.example.ticket_booking_system.exceptions.UserNotFoundException;
import com.example.ticket_booking_system.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    public UserResponse getResponse(User user){
        return new UserResponse(user.getId(), user.getName(),user.getRole());
    }
    public UserResponse addUser(UserRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setRole(request.getRole());
        User saved = userRepository.save(user);
        return getResponse(saved);
    }
    public UserResponse getUserById(Long id){
        return getResponse(userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id)));
    }
    public UserResponse updateUser(Long id, UserRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
        user.setRole(request.getRole());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        User saved = userRepository.save(user);
        return getResponse(saved);
    }
    public List<UserResponse> findUserByName(String name){
        return userRepository.findUserByNameContaining(name)
                .stream()
                .map(this::getResponse)
                .collect(Collectors.toList());
    }
    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException(id));
        userRepository.delete(user);
    }
    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(this::getResponse)
                .collect(Collectors.toList());
    }
}
