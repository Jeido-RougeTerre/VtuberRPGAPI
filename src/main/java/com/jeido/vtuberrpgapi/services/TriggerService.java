package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOReceive;
import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveCreation;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveUpdate;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendFull;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendLess;
import com.jeido.vtuberrpgapi.entites.StatInfluence;
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
import java.util.stream.Collectors;


@Service
public class TriggerService {

    private final VtuberRepository vtuberRepository;
    private final TriggerRepository triggerRepository;
    private final StatRepository statRepository;
    private final StatInfluenceService statInfluenceService;

    public TriggerService(VtuberRepository vtuberRepository, TriggerRepository triggerRepository, StatRepository statRepository, StatInfluenceService statInfluenceService) {
        this.vtuberRepository = vtuberRepository;
        this.triggerRepository = triggerRepository;
        this.statRepository = statRepository;
        this.statInfluenceService = statInfluenceService;
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
        if (triggers != null) {
            for (Trigger trigger : triggers) {
                dtos.add(toDTOLess(trigger));
            }
        }
        return dtos;
    }

    public TriggerDTOSendFull create(TriggerDTOReceiveCreation triggerDTOReceiveCreation) {
        Trigger triggerToSave = Trigger.builder()
                .id(new VtuberStringCompositeKey(vtuberRepository.findById(triggerDTOReceiveCreation.getVtuberId())
                        .orElseThrow(() -> new VtuberIdNotFoundException(triggerDTOReceiveCreation.getVtuberId())),
                        triggerDTOReceiveCreation.getLabel()))
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

    public TriggerDTOSendFull update(String label, UUID vtuberId, TriggerDTOReceiveUpdate triggerDTOReceiveUpdate) {
        Vtuber parsedVtuber = vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId));
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(parsedVtuber, label);
        Trigger triggerToUpdate = triggerRepository.findById(key).orElseThrow(() -> new TriggerNotFoundException(label, vtuberId));

        if (triggerDTOReceiveUpdate.getLabel() != null && !triggerDTOReceiveUpdate.getLabel().equals(triggerToUpdate.getId().getLabel())) {
            VtuberStringCompositeKey newKey = new VtuberStringCompositeKey(parsedVtuber, triggerDTOReceiveUpdate.getLabel());
            if (triggerRepository.existsById(newKey)) {
                throw new TriggerLabelForVtuberIdAlreadyInDBException(label, vtuberId);
            }

            triggerToUpdate.setId(newKey);
            triggerRepository.deleteById(key);
        }

        if (triggerDTOReceiveUpdate.getInfluences() != null && !triggerDTOReceiveUpdate.getInfluences().isEmpty()) {
            List<UUID> statInfluencesId = triggerToUpdate.getInfluences().stream().map(StatInfluence::getId).toList();
            for (StatInfluenceDTOSend influenceDTOSend : triggerDTOReceiveUpdate.getInfluences()) {
                if (statInfluencesId.contains(influenceDTOSend.getId())) {
                    statInfluenceService.update(influenceDTOSend.getId(), StatInfluenceDTOReceive.builder()
                                    .vtuberId(influenceDTOSend.getVtuberId())
                                    .triggerLabel(triggerDTOReceiveUpdate.getLabel())
                                    .statLabel(influenceDTOSend.getStatLabel())
                                    .value(influenceDTOSend.getValue())
                                    .operator(influenceDTOSend.getOperator())
                            .build());
                } else {
                    statInfluenceService.create(StatInfluenceDTOReceive.builder()
                            .triggerLabel(triggerDTOReceiveUpdate.getLabel())
                            .statLabel(influenceDTOSend.getStatLabel())
                            .value(influenceDTOSend.getValue())
                            .operator(influenceDTOSend.getOperator())
                            .build());
                }
            }
        }

        return toDTOFull(triggerRepository.save(triggerToUpdate));
    }

    public boolean delete(String label, UUID vtuberId) {
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), label);
        if(!triggerRepository.existsById(key)) return false;
        statInfluenceService.deleteForTrigger(vtuberId, label);
        triggerRepository.deleteById(key);
        return !triggerRepository.existsById(key);
    }

    public boolean delete(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) return false;
        statInfluenceService.deleteForVtuber(vtuberId);
        triggerRepository.deleteAll(triggerRepository.findByVtuberId(vtuberId));
        return statRepository.findByVtuberId(vtuberId).isEmpty();
    }
}