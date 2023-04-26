package com.wcs.project3.controller;

import com.wcs.project3.entity.User;
import com.wcs.project3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public User getProfile(@PathVariable String username) {
        return userRepository.findByUsername(username).get();
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public boolean deleteUser(@PathVariable String username) {
        User userToDelete = userRepository.findByUsername(username).get();
        userRepository.deleteById(userToDelete.getId());
        return true;
    }
}
