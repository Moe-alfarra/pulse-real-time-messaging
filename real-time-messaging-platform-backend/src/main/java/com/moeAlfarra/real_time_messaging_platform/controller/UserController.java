package com.moeAlfarra.real_time_messaging_platform.controller;

import com.moeAlfarra.real_time_messaging_platform.dto.UserResponse;
import com.moeAlfarra.real_time_messaging_platform.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getUser(authentication.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<String> updateUserName(Authentication authentication, @RequestParam String name) {
        userService.updateUserName(authentication.getName(), name);
        return ResponseEntity.ok("Name updated  successfully");
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> updateUserPassword(Authentication authentication, @RequestParam String currentPassword,
                                                     @RequestParam String newPassword) {
        userService.updatePassword(authentication.getName(), currentPassword, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }
    @GetMapping
    public List<UserResponse> getAllUsers(Principal principal) {
        return userService.getAllUsers(principal.getName());
    }
}
