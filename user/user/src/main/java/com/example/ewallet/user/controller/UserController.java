package com.example.ewallet.user.controller;

import com.example.ewallet.user.domain.User;
import com.example.ewallet.user.service.UserService;
import com.example.ewallet.user.service.resource.TransactionRequest;
import com.example.ewallet.user.service.resource.UserRequest;
import com.example.ewallet.user.service.resource.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("users")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest userRequest) {
        userService.createUser(userRequest.toUser());
        Map<String, String> response = new HashMap<>();
        response.put("message", "User created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<UserResponse> getUsers(@PathVariable("id") String id) {
        UserResponse user = userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        User user = userService.deleteUser(id);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    @PostMapping("users/{userId}/transfer")
    public ResponseEntity<?> transferFunds(@PathVariable("userId") String userId, @RequestBody @Valid TransactionRequest transactionRequest) {
        boolean success = userService.transferFunds(Long.valueOf(userId), transactionRequest);
        if(!success) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @PostMapping("users/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest userRequest) {
        UserResponse user = userService.login(userRequest.getEmail(), userRequest.getUsername(),userRequest.getPassword()).toUserResponse();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
