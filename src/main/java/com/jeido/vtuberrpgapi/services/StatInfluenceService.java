package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOReceive;
import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import com.jeido.vtuberrpgapi.entites.Stat;
import com.jeido.vtuberrpgapi.entites.StatInfluence;
import com.jeido.vtuberrpgapi.entites.Trigger;
import com.jeido.vtuberrpgapi.entites.Vtuber;
import com.jeido.vtuberrpgapi.entites.keys.VtuberStringCompositeKey;
import com.jeido.vtuberrpgapi.repositories.StatInfluenceRepository;
import com.jeido.vtuberrpgapi.repositories.StatRepository;
import com.jeido.vtuberrpgapi.repositories.TriggerRepository;
import com.jeido.vtuberrpgapi.repositories.VtuberRepository;
import com.jeido.vtuberrpgapi.utils.exceptions.stat.StatNotFoundException;
import com.jeido.vtuberrpgapi.utils.exceptions.stat_influence.NotMatchingVtuberIdException;
import com.jeido.vtuberrpgapi.utils.exceptions.stat_influence.StatInfluenceNotFoundException;
import com.jeido.vtuberrpgapi.utils.exceptions.trigger.TriggerNotFoundException;
import com.jeido.vtuberrpgapi.utils.exceptions.vtuber.VtuberIdNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StatInfluenceService {
    private final TriggerRepository triggerRepository;
    private final VtuberRepository vtuberRepository;
    private final StatRepository statRepository;
    private final StatInfluenceRepository statInfluenceRepository;

    public StatInfluenceService(TriggerRepository triggerRepository, VtuberRepository vtuberRepository, StatRepository statRepository, StatInfluenceRepository statInfluenceRepository) {
        this.triggerRepository = triggerRepository;
        this.vtuberRepository = vtuberRepository;
        this.statRepository = statRepository;
        this.statInfluenceRepository = statInfluenceRepository;
    }

    public StatInfluenceDTOSend toDTO(StatInfluence statInfluence) {
        return StatInfluenceDTOSend.builder()
                .id(statInfluence.getId())
                .vtuberId(statInfluence.getStat().getId().getVtuber().getId())
                .statLabel(statInfluence.getStat().getId().getLabel())
                .triggerLabel(statInfluence.getTrigger().getId().getLabel())
                .value(statInfluence.getValue())
                .operator(statInfluence.getOperator())
                .build();
    }

    public List<StatInfluenceDTOSend> toDTO(List<StatInfluence> statInfluences) {
        List<StatInfluenceDTOSend> statInfluenceDTOSends = new ArrayList<>();
        for (StatInfluence statInfluence : statInfluences) {
            statInfluenceDTOSends.add(toDTO(statInfluence));
        }
        return statInfluenceDTOSends;
    }

    public StatInfluenceDTOSend create(StatInfluenceDTOReceive influenceDTOReceive) {
        Vtuber vtuber = vtuberRepository.findById(influenceDTOReceive.getVtuberId()).orElseThrow(() -> new VtuberIdNotFoundException(influenceDTOReceive.getVtuberId()));
        VtuberStringCompositeKey tKey = new VtuberStringCompositeKey(vtuber, influenceDTOReceive.getTriggerLabel());
        Trigger trigger = triggerRepository.findById(tKey).orElseThrow(() -> new TriggerNotFoundException(influenceDTOReceive.getTriggerLabel(), influenceDTOReceive.getVtuberId()));

        VtuberStringCompositeKey sKey = new VtuberStringCompositeKey(vtuber, influenceDTOReceive.getStatLabel());
        Stat stat = statRepository.findById(sKey).orElseThrow(() -> new StatNotFoundException(influenceDTOReceive.getStatLabel(), influenceDTOReceive.getVtuberId()));

        if (!trigger.getId().getVtuber().getId().equals(stat.getId().getVtuber().getId()))
            throw new NotMatchingVtuberIdException(
                    trigger.getId().getVtuber().getId(),
                    stat.getId().getVtuber().getId()
            );

        StatInfluence influenceToSave = statInfluenceRepository.save(StatInfluence.builder()
                        .stat(stat)
                        .trigger(trigger)
                        .value(influenceDTOReceive.getValue())
                        .operator(influenceDTOReceive.getOperator())
                        .build());

        trigger.getInfluences().add(influenceToSave);
        stat.getInfluences().add(influenceToSave);

        return toDTO(influenceToSave);
    }

    public List<StatInfluenceDTOSend> findAll() {
        return toDTO((List<StatInfluence>) statInfluenceRepository.findAll());
    }

    public StatInfluenceDTOSend find(UUID id) {
        return toDTO(statInfluenceRepository.findById(id).orElseThrow(() -> new StatInfluenceNotFoundException(id)));
    }

    public List<StatInfluenceDTOSend> findForVtuber(UUID id) {
        return toDTO(statInfluenceRepository.findByVtuberId(id));
    }

    public List<StatInfluenceDTOSend> findForTrigger(UUID vtuberId, String triggerLabel) {
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), triggerLabel);
        Trigger trigger = triggerRepository.findById(key).orElseThrow(() -> new TriggerNotFoundException(triggerLabel, vtuberId));
        return toDTO(statInfluenceRepository.findByTrigger(trigger));
    }

    public List<StatInfluenceDTOSend> findForStat(UUID vtuberId, String statLabel) {
        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), statLabel);
        Stat stat = statRepository.findById(key).orElseThrow(() -> new StatNotFoundException(statLabel, vtuberId));
        return toDTO(statInfluenceRepository.findByStat(stat));
    }

    public StatInfluenceDTOSend update(UUID id, StatInfluenceDTOReceive influenceDTOReceive) {
        StatInfluence statInfluenceToUpdate = statInfluenceRepository.findById(id).orElseThrow(() -> new StatInfluenceNotFoundException(id));

        if (!influenceDTOReceive.getStatLabel().equals(statInfluenceToUpdate.getStat().getId().getLabel())) {
            Stat newStat = statRepository.findById(new VtuberStringCompositeKey(vtuberRepository.findById(statInfluenceToUpdate.getStat().getId().getVtuber().getId()).orElseThrow(() -> new VtuberIdNotFoundException(statInfluenceToUpdate.getStat().getId().getVtuber().getId())), influenceDTOReceive.getStatLabel())).orElseThrow(() -> new StatNotFoundException(influenceDTOReceive.getStatLabel(), statInfluenceToUpdate.getStat().getId().getVtuber().getId()));
            statInfluenceToUpdate.getStat().getInfluences().remove(statInfluenceToUpdate);
            statInfluenceToUpdate.setStat(newStat);
            statInfluenceToUpdate.getStat().getInfluences().add(statInfluenceToUpdate);
        }

        if (influenceDTOReceive.getValue() != null && !influenceDTOReceive.getValue().equals(statInfluenceToUpdate.getValue())) {
            statInfluenceToUpdate.setValue(influenceDTOReceive.getValue());
        }

        if (influenceDTOReceive.getOperator() != null && !influenceDTOReceive.getOperator().equals(statInfluenceToUpdate.getOperator())) {
            statInfluenceToUpdate.setOperator(influenceDTOReceive.getOperator());
        }

        return toDTO(statInfluenceRepository.save(statInfluenceToUpdate));
    }

    public boolean deleteAll() {
        statInfluenceRepository.deleteAll();
        return statInfluenceRepository.count() == 0;
    }

    public boolean delete(UUID id) {
        if (!statInfluenceRepository.existsById(id)) return false;
        statInfluenceRepository.deleteById(id);
        return !statInfluenceRepository.existsById(id);
    }

    public boolean deleteForVtuber(UUID vtuberId) {
        if (!statInfluenceRepository.existsByVtuberId(vtuberId)) return false;
        statInfluenceRepository.deleteAll(statInfluenceRepository.findByVtuberId(vtuberId));
        return !statInfluenceRepository.existsByVtuberId(vtuberId);
    }

    public boolean deleteForTrigger(UUID vtuberId, String triggerLabel) {
        if (!statInfluenceRepository.existsByVtuberId(vtuberId)) return false;

        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), triggerLabel);
        Trigger trigger = triggerRepository.findById(key).orElse(null);
        if (trigger == null) return false;

        if (!statInfluenceRepository.existsByTrigger(trigger)) return false;

        statInfluenceRepository.deleteAll(statInfluenceRepository.findByTrigger(trigger));

        return !statInfluenceRepository.existsByTrigger(trigger);
    }

    public boolean deleteForStat(UUID vtuberId, String statLabel) {
        if (!statInfluenceRepository.existsByVtuberId(vtuberId)) return false;

        VtuberStringCompositeKey key = new VtuberStringCompositeKey(vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), statLabel);
        Stat stat = statRepository.findById(key).orElse(null);
        if (stat == null) return false;

        if (!statInfluenceRepository.existsByStat(stat)) return false;
        statInfluenceRepository.deleteAll(statInfluenceRepository.findByStat(stat));
        return !statInfluenceRepository.existsByStat(stat);
    }
}
