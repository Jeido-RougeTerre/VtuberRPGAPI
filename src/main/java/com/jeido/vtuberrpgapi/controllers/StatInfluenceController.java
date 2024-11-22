package com.jeido.vtuberrpgapi.controllers;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOReceive;
import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import com.jeido.vtuberrpgapi.services.StatInfluenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/influences")
public class StatInfluenceController {

    private final StatInfluenceService statInfluenceService;

    public StatInfluenceController(StatInfluenceService statInfluenceService) {
        this.statInfluenceService = statInfluenceService;
    }

    @GetMapping
    public ResponseEntity<List<StatInfluenceDTOSend>> getStatInfluences(@RequestParam(value = "vtuber", required = false)UUID vtuberId, @RequestParam(value = "statId", required = false)String statLabel, @RequestParam(value = "trigger", required = false)String triggerLabel) {
        if (vtuberId != null) {
            if (triggerLabel != null) {
                return ResponseEntity.ok(statInfluenceService.findForTrigger(vtuberId, triggerLabel));
            }
            if (statLabel != null) {
                return ResponseEntity.ok(statInfluenceService.findForStat(vtuberId, statLabel));
            }
            return ResponseEntity.ok(statInfluenceService.findForVtuber(vtuberId));
        }
        return ResponseEntity.ok(statInfluenceService.findAll());
    }

    @PostMapping
    public ResponseEntity<StatInfluenceDTOSend> createStatInfluence(@Valid @RequestBody StatInfluenceDTOReceive influenceDTO) {
        return new ResponseEntity<>(statInfluenceService.create(influenceDTO), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteStatInfluences(@RequestParam(value = "vtuber", required = false)UUID vtuberId, @RequestParam(value = "trigger", required = false)UUID triggerId, @RequestParam(value = "statId", required = false)UUID statId) {
        if (vtuberId != null) {
            return ResponseEntity.ok(statInfluenceService.deleteForVtuber(vtuberId));
        }
        if (triggerId != null) {
            return ResponseEntity.ok(statInfluenceService.deleteForTrigger(triggerId));
        }
        if (statId != null) {
            return ResponseEntity.ok(statInfluenceService.deleteForStat(statId));
        }
        return ResponseEntity.ok(statInfluenceService.deleteAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StatInfluenceDTOSend> getStatInfluence(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(statInfluenceService.find(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StatInfluenceDTOSend> updateStatInfluence(@PathVariable("id") UUID id, @Valid @RequestBody StatInfluenceDTOReceive influenceDTO) {
        return ResponseEntity.ok(statInfluenceService.update(id, influenceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteStatInfluence(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(statInfluenceService.delete(id));
    }
}
