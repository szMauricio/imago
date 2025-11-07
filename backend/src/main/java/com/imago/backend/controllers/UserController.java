package com.imago.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imago.backend.models.User;
import com.imago.backend.models.dto.UserDto;
import com.imago.backend.services.JwtService;
import com.imago.backend.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private final JwtService jwtService;

    public UserController(UserService service, JwtService jwtService) {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto.Response> registerUser(@Valid @RequestBody UserDto.CreateRequest request) {
        User user = new User(
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPassword(),
                null);

        User createdUser = service.createUser(user);
        UserDto.Response response = new UserDto.Response(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@Valid @RequestBody UserDto.LoginRequest request) {
        User user = service.validateLogin(request.getEmail(), request.getPassword());
        String token = jwtService.generateToken(user);

        UserDto.Response userResponse = new UserDto.Response(user);
        UserDto.LoginResponse loginResponse = new UserDto.LoginResponse(token, userResponse);

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping
    public ResponseEntity<List<UserDto.Response>> getAllUsers() {
        List<UserDto.Response> users = service.getAllUsers().stream().map(UserDto.Response::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> getUserById(@PathVariable Long id) {
        User user = service.getUserById(id);
        UserDto.Response response = new UserDto.Response(user);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto.Response> updateUser(@PathVariable Long id,
            @Valid @RequestBody UserDto.UpdateRequest request) {
        User userDetails = new User();
        userDetails.setEmail(request.getEmail());
        userDetails.setFirstName(request.getFirstName());
        userDetails.setLastName(request.getLastName());
        userDetails.setPassword(request.getPassword());

        User updatedUser = service.updateUser(id, userDetails);
        UserDto.Response response = new UserDto.Response(updatedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
