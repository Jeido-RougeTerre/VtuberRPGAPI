package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.stat.StatDTOSendLess;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveCreation;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveUpdate;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendFull;
import com.jeido.vtuberrpgapi.services.StatInfluenceService;
import com.jeido.vtuberrpgapi.services.TriggerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/triggers")
public class TriggerController {

    private final TriggerService triggerService;
    private final StatInfluenceService statInfluenceService;

    public TriggerController(TriggerService triggerService, StatInfluenceService statInfluenceService) {
        this.triggerService = triggerService;
        this.statInfluenceService = statInfluenceService;
    }

    @GetMapping
    public ResponseEntity<List<TriggerDTOSendFull>> getTriggers(@RequestParam(value = "vtuber", required = false)UUID vtuberId, @RequestParam(value = "label", required = false)String label) {
        if (vtuberId == null) {
            return ResponseEntity.ok(triggerService.findAll());
        }

        if (label == null) {
            return ResponseEntity.ok(triggerService.findByVtuberId(vtuberId));
        }

        return ResponseEntity.ok(Collections.singletonList(triggerService.findByLabelAndVtuberId(label, vtuberId)));
    }

    @PostMapping
    public ResponseEntity<TriggerDTOSendFull> createTrigger(@Valid @RequestBody TriggerDTOReceiveCreation triggerDTOReceiveCreation) {
        return new ResponseEntity<>(triggerService.create(triggerDTOReceiveCreation), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteTriggerByVtuber(@RequestParam(value = "vtuber")UUID vtuberId) {
        return ResponseEntity.ok(triggerService.deleteByVtuber(vtuberId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TriggerDTOSendFull> getTriggerByID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(triggerService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TriggerDTOSendFull> updateTrigger(@PathVariable("id") UUID id, @Valid @RequestBody TriggerDTOReceiveUpdate triggerDTOReceiveUpdate) {
        return ResponseEntity.ok(triggerService.update(id, triggerDTOReceiveUpdate));
    }

    @PostMapping("/{id}")
    public ResponseEntity<List<StatDTOSendLess>> trigger(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(statInfluenceService.act(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTriggerByID(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(triggerService.delete(id));
    }
}
