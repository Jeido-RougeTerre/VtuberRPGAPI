package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.stat.StatDTOReceiveFull;
import com.jeido.vtuberrpgapi.dto.stat.StatDTOReceiveLess;
import com.jeido.vtuberrpgapi.dto.stat.StatDTOSendFull;
import com.jeido.vtuberrpgapi.services.StatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stats")
public class StatController {

    private final StatService statService;

    public StatController(StatService statService) {
        this.statService = statService;
    }

    @GetMapping
    public ResponseEntity<List<StatDTOSendFull>> getAllStats(@RequestParam(value = "vtuber", required = false)UUID vtuberId, @RequestParam(value = "label", required = false) String label) {
        if (vtuberId == null) {
            return ResponseEntity.ok(statService.findAll());
        }

        if (label == null) {
            return ResponseEntity.ok(statService.findByVtuberId(vtuberId));
        }

        return ResponseEntity.ok(Collections.singletonList(statService.findByVtuberIdAndLabel(vtuberId, label)));
    }

    @PostMapping
    public ResponseEntity<StatDTOSendFull> createStat(@Validated @Valid @RequestBody StatDTOReceiveFull statDTOReceiveFull) {
        return new ResponseEntity<>(statService.create(statDTOReceiveFull), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> DeleteStatByVtuber(@RequestParam(value = "vtuber")UUID vtuberId) {
        return ResponseEntity.ok(statService.deleteByVtuber(vtuberId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatDTOSendFull> getStatByID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(statService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatDTOSendFull> updateStat(@PathVariable("id") UUID statId, @Valid @RequestBody StatDTOReceiveLess statDTOReceiveLess) {
        return ResponseEntity.ok(statService.update(statId, statDTOReceiveLess));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteStat(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(statService.delete(id));
    }

}
