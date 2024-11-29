package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.vtuber.VtuberDTOReceive;
import com.jeido.vtuberrpgapi.dto.vtuber.VtuberDTOSend;
import com.jeido.vtuberrpgapi.services.VtuberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vtubers")
public class VtuberController {

    private final VtuberService vtuberService;

    @Autowired
    public VtuberController(VtuberService vtuberService) {
        this.vtuberService = vtuberService;
    }

    @GetMapping
    public ResponseEntity<List<VtuberDTOSend>> getAll() {
        return ResponseEntity.ok(vtuberService.findAll());
    }

    @PostMapping
    public ResponseEntity<VtuberDTOSend> create(@Valid @RequestBody VtuberDTOReceive vtuberDTOReceive, @RequestParam(value = "user_id", required = false) UUID user_id) {
        if (user_id != null) {
            return new ResponseEntity<>(vtuberService.create(vtuberDTOReceive, user_id), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(vtuberService.create(vtuberDTOReceive), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VtuberDTOSend> get(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(vtuberService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VtuberDTOSend> update(@PathVariable("id") UUID id, @Valid @RequestBody VtuberDTOReceive vtuberDTOReceive) {
        return ResponseEntity.ok(vtuberService.update(id, vtuberDTOReceive));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(vtuberService.delete(id));
    }


}
