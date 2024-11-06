package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.UserDTOLogin;
import com.jeido.vtuberrpgapi.dto.UserDTOReceive;
import com.jeido.vtuberrpgapi.dto.UserDTOSend;
import com.jeido.vtuberrpgapi.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTOSend>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserDTOSend> register(@Valid @RequestBody UserDTOReceive userDTOReceive) {
        return new ResponseEntity<>(userService.create(userDTOReceive), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserDTOSend> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDTOSend> update(@PathVariable UUID id, @RequestBody @Valid UserDTOReceive userDTOReceive) {
        return ResponseEntity.ok(userService.update(id, userDTOReceive));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.delete(id));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTOSend> login(@RequestBody @Valid UserDTOLogin userDTOLogin) {
        return ResponseEntity.ok(userService.login(userDTOLogin));
    }

}
