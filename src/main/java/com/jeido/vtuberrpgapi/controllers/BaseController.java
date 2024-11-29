package com.jeido.vtuberrpgapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BaseController {

    @GetMapping
    public ResponseEntity<String> get(Authentication auth) {
        return ResponseEntity.ok("Hello, " + auth.getName() + "!\nVtuber RPG API is working !\n☆*: .｡. o(≧▽≦)o .｡.:*☆");
    }
}
