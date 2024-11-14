package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.TriggerDTOReceive;
import com.jeido.vtuberrpgapi.dto.TriggerDTOSend;
import com.jeido.vtuberrpgapi.entites.Trigger;
import com.jeido.vtuberrpgapi.entites.Vtuber;
import com.jeido.vtuberrpgapi.entites.keys.VtuberStringCompositeKey;
import com.jeido.vtuberrpgapi.repositories.StatRepository;
import com.jeido.vtuberrpgapi.repositories.TriggerRepository;
import com.jeido.vtuberrpgapi.repositories.VtuberRepository;
import com.jeido.vtuberrpgapi.utils.exceptions.trigger.TriggerLabelForVtuberIdAlreadyInDBException;
import com.jeido.vtuberrpgapi.utils.exceptions.trigger.TriggerNotFoundException;
import com.jeido.vtuberrpgapi.utils.exceptions.vtuber.VtuberIdNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class TriggerService {

    private final VtuberRepository vtuberRepository;
    private final TriggerRepository triggerRepository;
    private final StatRepository statRepository;

    public TriggerService(VtuberRepository vtuberRepository, TriggerRepository triggerRepository, StatRepository statRepository) {
        this.vtuberRepository = vtuberRepository;
        this.triggerRepository = triggerRepository;
        this.statRepository = statRepository;
    }

    public TriggerDTOSend toDTO(Trigger trigger) {
        return TriggerDTOSend.builder()
                .label(trigger.getId().getLabel())
                .vtuberId(trigger.getId().getVtuber().getId())
                .build();
    }

    public List<TriggerDTOSend> toDTO(List<Trigger> triggers) {
        List<TriggerDTOSend> dtos = new ArrayList<>();
        for (Trigger trigger : triggers) {
            dtos.add(toDTO(trigger));
        }
        return dtos;
    }

    public TriggerDTOSend create(TriggerDTOReceive triggerDTOReceive) {
        Trigger triggerToSave = Trigger.builder()
                .id(new VtuberStringCompositeKey(vtuberRepository.findById(triggerDTOReceive.getVtuberId())
                        .orElseThrow(() -> new VtuberIdNotFoundException(triggerDTOReceive.getVtuberId())),
                        triggerDTOReceive.getLabel()))
                .build();
        if (triggerRepository.existsById(triggerToSave.getId())) {
            throw new TriggerLabelForVtuberIdAlreadyInDBException(triggerToSave.getId().getLabel(), triggerToSave.getId().getVtuber().getId());
        }

        return toDTO(triggerRepository.save(triggerToSave));
    }

    public TriggerDTOSend create(UUID vtuberId, String label) {
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), label);
        if (statRepository.existsById(key)) throw new TriggerLabelForVtuberIdAlreadyInDBException(label, vtuberId);

        return toDTO(triggerRepository.save(Trigger.builder()
                .id(key)
                .build()
        ));
    }

    public List<TriggerDTOSend> findAll() {
        return toDTO((List<Trigger>) triggerRepository.findAll());
    }

    public List<TriggerDTOSend> findByVtuberId(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return toDTO(triggerRepository.findByVtuberId(vtuberId));
    }

    public TriggerDTOSend findByLabelAndVtuberId(String label, UUID vtuberId) {
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), label);
        return toDTO(triggerRepository.findById(key).orElseThrow(() -> new TriggerNotFoundException(label, vtuberId)));
    }

    public TriggerDTOSend update(String label, UUID vtuberId, TriggerDTOReceive triggerDTOReceive) {
        Vtuber parsedVtuber = vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId));
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(parsedVtuber, label);
        Trigger triggerToUpdate = triggerRepository.findById(key).orElseThrow(() -> new TriggerNotFoundException(label, vtuberId));

        if (triggerDTOReceive.getLabel() != null && !triggerDTOReceive.getLabel().equals(triggerToUpdate.getId().getLabel())) {
            VtuberStringCompositeKey newKey = new VtuberStringCompositeKey(parsedVtuber, triggerDTOReceive.getLabel());
            if (triggerRepository.existsById(newKey)) {
                throw new TriggerLabelForVtuberIdAlreadyInDBException(label, vtuberId);
            }

            triggerToUpdate.setId(newKey);
            triggerRepository.deleteById(key);
        }

        return toDTO(triggerRepository.save(triggerToUpdate));
    }

    public boolean delete(String label, UUID vtuberId) {
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), label);
        if(!triggerRepository.existsById(key)) return false;
        triggerRepository.deleteById(key);
        return !triggerRepository.existsById(key);
    }

    public boolean delete(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) return false;
        triggerRepository.deleteAll(triggerRepository.findByVtuberId(vtuberId));
        return statRepository.findByVtuberId(vtuberId).isEmpty();
    }
}