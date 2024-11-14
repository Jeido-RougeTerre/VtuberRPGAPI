package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveFull;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveLess;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendFull;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendLess;
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

    public TriggerDTOSendFull toDTOFull(Trigger trigger) {
        return TriggerDTOSendFull.builder()
                .label(trigger.getId().getLabel())
                .vtuberId(trigger.getId().getVtuber().getId())
                .build();
    }

    public List<TriggerDTOSendFull> toDTOFull(List<Trigger> triggers) {
        List<TriggerDTOSendFull> dtos = new ArrayList<>();
        for (Trigger trigger : triggers) {
            dtos.add(toDTOFull(trigger));
        }
        return dtos;
    }

    public TriggerDTOSendLess toDTOLess(Trigger trigger) {
        return TriggerDTOSendLess.builder()
                .label(trigger.getId().getLabel())
                .build();
    }

    public List<TriggerDTOSendLess> toDTOLess(List<Trigger> triggers) {
        List<TriggerDTOSendLess> dtos = new ArrayList<>();
        for (Trigger trigger : triggers) {
            dtos.add(toDTOLess(trigger));
        }
        return dtos;
    }

    public TriggerDTOSendFull create(TriggerDTOReceiveFull triggerDTOReceiveFull) {
        Trigger triggerToSave = Trigger.builder()
                .id(new VtuberStringCompositeKey(vtuberRepository.findById(triggerDTOReceiveFull.getVtuberId())
                        .orElseThrow(() -> new VtuberIdNotFoundException(triggerDTOReceiveFull.getVtuberId())),
                        triggerDTOReceiveFull.getLabel()))
                .build();
        if (triggerRepository.existsById(triggerToSave.getId())) {
            throw new TriggerLabelForVtuberIdAlreadyInDBException(triggerToSave.getId().getLabel(), triggerToSave.getId().getVtuber().getId());
        }

        return toDTOFull(triggerRepository.save(triggerToSave));
    }

    public TriggerDTOSendFull create(UUID vtuberId, String label) {
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), label);
        if (statRepository.existsById(key)) throw new TriggerLabelForVtuberIdAlreadyInDBException(label, vtuberId);

        return toDTOFull(triggerRepository.save(Trigger.builder()
                .id(key)
                .build()
        ));
    }

    public List<TriggerDTOSendFull> findAll() {
        return toDTOFull((List<Trigger>) triggerRepository.findAll());
    }

    public List<TriggerDTOSendLess> findByVtuberId(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return toDTOLess(triggerRepository.findByVtuberId(vtuberId));
    }

    public TriggerDTOSendLess findByLabelAndVtuberId(String label, UUID vtuberId) {
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), label);
        return toDTOLess(triggerRepository.findById(key).orElseThrow(() -> new TriggerNotFoundException(label, vtuberId)));
    }

    public TriggerDTOSendFull update(String label, UUID vtuberId, TriggerDTOReceiveLess triggerDTOReceiveLess) {
        Vtuber parsedVtuber = vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId));
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(parsedVtuber, label);
        Trigger triggerToUpdate = triggerRepository.findById(key).orElseThrow(() -> new TriggerNotFoundException(label, vtuberId));

        if (triggerDTOReceiveLess.getLabel() != null && !triggerDTOReceiveLess.getLabel().equals(triggerToUpdate.getId().getLabel())) {
            VtuberStringCompositeKey newKey = new VtuberStringCompositeKey(parsedVtuber, triggerDTOReceiveLess.getLabel());
            if (triggerRepository.existsById(newKey)) {
                throw new TriggerLabelForVtuberIdAlreadyInDBException(label, vtuberId);
            }

            triggerToUpdate.setId(newKey);
            triggerRepository.deleteById(key);
        }

        return toDTOFull(triggerRepository.save(triggerToUpdate));
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