package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.base.BaseDTOSend;
import com.jeido.vtuberrpgapi.dto.base.EnumDTOSend;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/")
public class BaseController {

    @GetMapping
    public ResponseEntity<BaseDTOSend> get() {
        return ResponseEntity.ok(new BaseDTOSend());
    }

    @GetMapping("utils/")
    public ResponseEntity<EnumDTOSend> getEnums() {
        return ResponseEntity.ok(new EnumDTOSend());
    }
}
