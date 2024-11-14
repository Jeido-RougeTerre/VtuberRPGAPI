package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.TriggerDTOReceive;
import com.jeido.vtuberrpgapi.dto.TriggerDTOSend;
import com.jeido.vtuberrpgapi.services.TriggerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/triggers")
public class TriggerController {

    private final TriggerService triggerService;

    public TriggerController(TriggerService triggerService) {
        this.triggerService = triggerService;
    }

    @GetMapping
    public ResponseEntity<List<TriggerDTOSend>> getTriggers() {
        return ResponseEntity.ok(triggerService.findAll());
    }

    @PostMapping
    public ResponseEntity<TriggerDTOSend> createTrigger(@Valid @RequestBody TriggerDTOReceive triggerDTOReceive) {
        return new ResponseEntity<>(triggerService.create(triggerDTOReceive), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TriggerDTOSend>> getTriggerByVtuberID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(triggerService.findByVtuberId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAllTriggersForVtuberID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(triggerService.delete(id));
    }

    @GetMapping("/{id}/{label}")
    public ResponseEntity<TriggerDTOSend> findTrigger(@PathVariable("id") UUID id, @PathVariable("label") String label) {
        return ResponseEntity.ok(triggerService.findByLabelAndVtuberId(label, id));
    }

    @PostMapping("/{id}/{label}")
    public ResponseEntity<TriggerDTOSend> createTriggerForVtuberID(@PathVariable("id") UUID id, @PathVariable("label") String label) {
        return new ResponseEntity<>(triggerService.create(id, label), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{label}")
    public ResponseEntity<TriggerDTOSend> updateTrigger(@PathVariable("id") UUID id, @PathVariable("label") String label, @Valid @RequestBody TriggerDTOReceive triggerDTOReceive) {
        return ResponseEntity.ok(triggerService.update(label, id, triggerDTOReceive));
    }

    @DeleteMapping("/{id}/{label}")
    public ResponseEntity<Boolean> deleteTrigger(@PathVariable("id") UUID id, @PathVariable("label") String label) {
        return ResponseEntity.ok(triggerService.delete(label, id));
    }
}
