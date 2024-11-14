package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveFull;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveLess;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendFull;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendLess;
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
    public ResponseEntity<List<TriggerDTOSendFull>> getTriggers() {
        return ResponseEntity.ok(triggerService.findAll());
    }

    @PostMapping
    public ResponseEntity<TriggerDTOSendFull> createTrigger(@Valid @RequestBody TriggerDTOReceiveFull triggerDTOReceiveFull) {
        return new ResponseEntity<>(triggerService.create(triggerDTOReceiveFull), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<TriggerDTOSendLess>> getTriggerByVtuberID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(triggerService.findByVtuberId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAllTriggersForVtuberID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(triggerService.delete(id));
    }

    @GetMapping("/{id}/{label}")
    public ResponseEntity<TriggerDTOSendLess> findTrigger(@PathVariable("id") UUID id, @PathVariable("label") String label) {
        return ResponseEntity.ok(triggerService.findByLabelAndVtuberId(label, id));
    }

    @PostMapping("/{id}/{label}")
    public ResponseEntity<TriggerDTOSendFull> createTriggerForVtuberID(@PathVariable("id") UUID id, @PathVariable("label") String label) {
        return new ResponseEntity<>(triggerService.create(id, label), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/{label}")
    public ResponseEntity<TriggerDTOSendFull> updateTrigger(@PathVariable("id") UUID id, @PathVariable("label") String label, @Valid @RequestBody TriggerDTOReceiveLess triggerDTOReceiveLess) {
        return ResponseEntity.ok(triggerService.update(label, id, triggerDTOReceiveLess));
    }

    @DeleteMapping("/{id}/{label}")
    public ResponseEntity<Boolean> deleteTrigger(@PathVariable("id") UUID id, @PathVariable("label") String label) {
        return ResponseEntity.ok(triggerService.delete(label, id));
    }
}
