package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.StatDTOReceive;
import com.jeido.vtuberrpgapi.dto.StatDTOSend;
import com.jeido.vtuberrpgapi.services.StatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<StatDTOSend>> getAllStats() {
        return ResponseEntity.ok(statService.findAll());
    }

    @PostMapping
    public ResponseEntity<StatDTOSend> createStat(@Valid @RequestBody StatDTOReceive statDTOReceive) {
        return new ResponseEntity<>(statService.create(statDTOReceive), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<StatDTOSend>> getStatByVtuberID(@PathVariable("id") UUID vtuberID) {
        return ResponseEntity.ok(statService.findByVtuberId(vtuberID));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAllStatsForVtuberID(@PathVariable("id") UUID vtuberID) {
        return ResponseEntity.ok(statService.delete(vtuberID));
    }

    @GetMapping("/{id}/{label}")
    public ResponseEntity<StatDTOSend> getStatForVtuberID(@PathVariable("id") UUID vtuberID, @PathVariable("label") String label) {
        return ResponseEntity.ok(statService.findByVtuberIdAndLabel(vtuberID, label));
    }

    @PostMapping("/{id}/{label}")
    public ResponseEntity<StatDTOSend> createStat(@PathVariable("id") UUID vtuberID, @PathVariable("label") String label) {
        return new ResponseEntity<>(statService.create(vtuberID, label), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{label}")
    public ResponseEntity<StatDTOSend> updateStat(@PathVariable("id") UUID vtuberID, @PathVariable("label") String label, @Valid @RequestBody StatDTOReceive statDTOReceive) {
        return ResponseEntity.ok(statService.update(label, vtuberID, statDTOReceive));
    }



}
