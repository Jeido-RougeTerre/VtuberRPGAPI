package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOReceive;
import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveCreation;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOReceiveUpdate;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendFull;
import com.jeido.vtuberrpgapi.dto.trigger.TriggerDTOSendLess;
import com.jeido.vtuberrpgapi.entites.StatInfluence;
import com.jeido.vtuberrpgapi.entites.Trigger;
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
    private final StatInfluenceService statInfluenceService;

    public TriggerService(VtuberRepository vtuberRepository, TriggerRepository triggerRepository, StatRepository statRepository, StatInfluenceService statInfluenceService) {
        this.vtuberRepository = vtuberRepository;
        this.triggerRepository = triggerRepository;
        this.statRepository = statRepository;
        this.statInfluenceService = statInfluenceService;
    }

    public TriggerDTOSendFull toDTOFull(Trigger trigger) {
        return TriggerDTOSendFull.builder()
                .id(trigger.getId())
                .label(trigger.getLabel())
                .vtuberId(trigger.getVtuber().getId())
                .influences(statInfluenceService.toDTO(trigger.getInfluences()))
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
                .label(trigger.getLabel())
                .influences(statInfluenceService.toDTO(trigger.getInfluences()))
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
                .label(triggerDTOReceiveCreation.getLabel())
                .vtuber(vtuberRepository.findById(triggerDTOReceiveCreation.getVtuberId()).orElseThrow(() -> new VtuberIdNotFoundException(triggerDTOReceiveCreation.getVtuberId())))
                .build();
        if (triggerRepository.existsByVtuberIdAndLabel(triggerToSave.getVtuber().getId(), triggerToSave.getLabel())) {
            throw new TriggerLabelForVtuberIdAlreadyInDBException(triggerToSave.getLabel(), triggerToSave.getVtuber().getId());
        }

        return toDTOFull(triggerRepository.save(triggerToSave));
    }

    public TriggerDTOSendFull create(UUID vtuberId, String label) {
        if (statRepository.existsByVtuberIdAndLabel(vtuberId, label)) throw new TriggerLabelForVtuberIdAlreadyInDBException(label, vtuberId);

        return toDTOFull(triggerRepository.save(Trigger.builder()
                        .vtuber(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)))
                .label(label)
                .build()
        ));
    }

    public List<TriggerDTOSendFull> findAll() {
        return toDTOFull((List<Trigger>) triggerRepository.findAll());
    }

    public List<TriggerDTOSendFull> findByVtuberId(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return toDTOFull(triggerRepository.findByVtuberId(vtuberId));
    }

    public TriggerDTOSendFull findByLabelAndVtuberId(String label, UUID vtuberId) {
        return toDTOFull(triggerRepository.findByVtuberIdAndLabel(vtuberId, label).orElseThrow(() -> new TriggerNotFoundException(vtuberId, label)));
    }

    public TriggerDTOSendFull update(UUID id, TriggerDTOReceiveUpdate triggerDTOReceiveUpdate) {
        Trigger triggerToUpdate = triggerRepository.findById(id).orElseThrow(() -> new TriggerNotFoundException(id));

        if (triggerDTOReceiveUpdate.getLabel() != null && !triggerDTOReceiveUpdate.getLabel().equals(triggerToUpdate.getLabel())) {
            if (triggerRepository.existsByVtuberIdAndLabel(triggerToUpdate.getVtuber().getId(), triggerDTOReceiveUpdate.getLabel())) {
                throw new TriggerLabelForVtuberIdAlreadyInDBException(triggerDTOReceiveUpdate.getLabel(), triggerToUpdate.getVtuber().getId());
            }

            triggerToUpdate.setLabel(triggerDTOReceiveUpdate.getLabel());
        }

        if (triggerDTOReceiveUpdate.getInfluences() != null && !triggerDTOReceiveUpdate.getInfluences().isEmpty()) {
            List<UUID> statInfluencesId = triggerToUpdate.getInfluences().stream().map(StatInfluence::getId).toList();
            for (StatInfluenceDTOSend influenceDTOSend : triggerDTOReceiveUpdate.getInfluences()) {
                if (statInfluencesId.contains(influenceDTOSend.getId())) {
                    statInfluenceService.update(influenceDTOSend.getId(), StatInfluenceDTOReceive.builder()

                                    .triggerId(influenceDTOSend.getTriggerId())
                                    .statId(influenceDTOSend.getStatId())
                                    .value(influenceDTOSend.getValue())
                                    .operator(influenceDTOSend.getOperator())
                            .build());
                } else {
                    statInfluenceService.create(StatInfluenceDTOReceive.builder()
                            .triggerId(influenceDTOSend.getTriggerId())
                            .statId(influenceDTOSend.getStatId())
                            .value(influenceDTOSend.getValue())
                            .operator(influenceDTOSend.getOperator())
                            .build());
                }
            }
        }

        return toDTOFull(triggerRepository.save(triggerToUpdate));
    }

    public boolean delete(UUID id) {
        if(!triggerRepository.existsById(id)) return false;
        statInfluenceService.deleteForTrigger(id);
        triggerRepository.deleteById(id);
        return !triggerRepository.existsById(id);
    }

    public boolean deleteByVtuber(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) return false;
        statInfluenceService.deleteForVtuber(vtuberId);
        triggerRepository.deleteAll(triggerRepository.findByVtuberId(vtuberId));
        return statRepository.findByVtuberId(vtuberId).isEmpty();
    }

    public TriggerDTOSendFull findById(UUID id) {
        return toDTOFull(triggerRepository.findById(id).orElseThrow(() -> new TriggerNotFoundException(id)));
    }
}