package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.StatDTOReceive;
import com.jeido.vtuberrpgapi.dto.StatDTOSendFull;
import com.jeido.vtuberrpgapi.dto.StatDTOSendLess;
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
    public ResponseEntity<List<StatDTOSendFull>> getAllStats() {
        return ResponseEntity.ok(statService.findAll());
    }

    @PostMapping
    public ResponseEntity<StatDTOSendFull> createStat(@Valid @RequestBody StatDTOReceive statDTOReceive) {
        return new ResponseEntity<>(statService.create(statDTOReceive), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<StatDTOSendLess>> getStatByVtuberID(@PathVariable("id") UUID vtuberID) {
        return ResponseEntity.ok(statService.findByVtuberId(vtuberID));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAllStatsForVtuberID(@PathVariable("id") UUID vtuberID) {
        return ResponseEntity.ok(statService.delete(vtuberID));
    }

    @GetMapping("/{id}/{label}")
    public ResponseEntity<String> getStatForVtuberID(@PathVariable("id") UUID vtuberID, @PathVariable("label") String label) {
        return ResponseEntity.ok(statService.findByVtuberIdAndLabel(vtuberID, label));
    }

    @PostMapping("/{id}/{label}")
    public ResponseEntity<StatDTOSendFull> createStat(@PathVariable("id") UUID vtuberID, @PathVariable("label") String label) {
        return new ResponseEntity<>(statService.create(vtuberID, label), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{label}")
    public ResponseEntity<StatDTOSendFull> updateStat(@PathVariable("id") UUID vtuberID, @PathVariable("label") String label, @Valid @RequestBody StatDTOReceive statDTOReceive) {
        return ResponseEntity.ok(statService.update(label, vtuberID, statDTOReceive));
    }

    @DeleteMapping("/{id}/{label}")
    public ResponseEntity<Boolean> deleteStat(@PathVariable("id") UUID vtuberID, @PathVariable("label") String label) {
        return ResponseEntity.ok(statService.delete(label, vtuberID));
    }



}
