package com.moeAlfarra.real_time_messaging_platform.service;

import com.moeAlfarra.real_time_messaging_platform.dto.UserResponse;
import com.moeAlfarra.real_time_messaging_platform.entity.User;
import com.moeAlfarra.real_time_messaging_platform.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getUser(String currentUserEmail) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    public void updateUserName(String currentUserEmail, String newName) {
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (newName == null || newName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be empty");
        }

        user.setName(newName);
        userRepository.save(user);
    }

    public void updatePassword(String currentUserEmail, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (currentPassword == null || currentPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is required");
        }

        if (newPassword == null || newPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password is required");
        }

        if (newPassword.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be at least 6 characters");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<UserResponse> getAllUsers(String currentUserEmail) {
        List<User> users = userRepository.findByEmailNot(currentUserEmail);

        List<UserResponse> allUsers = new ArrayList<>();
        for (User user: users) {
            allUsers.add(new UserResponse(user.getId(), user.getName(), user.getEmail()));
        }

        return allUsers;
    }
}
