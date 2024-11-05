package com.jeido.vtuberrpgapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class BaseController {

    @GetMapping
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Vtuber RPG API is working !\n☆*: .｡. o(≧▽≦)o .｡.:*☆");
    }
}
