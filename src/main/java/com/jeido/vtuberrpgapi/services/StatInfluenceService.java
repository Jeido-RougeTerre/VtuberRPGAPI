package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.stat.StatDTOSendLess;
import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOReceive;
import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import com.jeido.vtuberrpgapi.entites.Stat;
import com.jeido.vtuberrpgapi.entites.StatInfluence;
import com.jeido.vtuberrpgapi.entites.Trigger;
import com.jeido.vtuberrpgapi.repositories.StatInfluenceRepository;
import com.jeido.vtuberrpgapi.repositories.StatRepository;
import com.jeido.vtuberrpgapi.repositories.TriggerRepository;
import com.jeido.vtuberrpgapi.utils.exceptions.stat.StatNotFoundException;
import com.jeido.vtuberrpgapi.utils.exceptions.stat_influence.NotMatchingVtuberIdException;
import com.jeido.vtuberrpgapi.utils.exceptions.stat_influence.StatInfluenceNotFoundException;
import com.jeido.vtuberrpgapi.utils.exceptions.stat_influence.TriggerCannotActException;
import com.jeido.vtuberrpgapi.utils.exceptions.trigger.TriggerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StatInfluenceService {
    private final TriggerRepository triggerRepository;
    private final StatRepository statRepository;
    private final StatInfluenceRepository statInfluenceRepository;

    public StatInfluenceService(TriggerRepository triggerRepository, StatRepository statRepository, StatInfluenceRepository statInfluenceRepository) {
        this.triggerRepository = triggerRepository;
        this.statRepository = statRepository;
        this.statInfluenceRepository = statInfluenceRepository;
    }

    public StatInfluenceDTOSend toDTO(StatInfluence statInfluence) {
        return StatInfluenceDTOSend.builder()
                .id(statInfluence.getId())
                .statId(statInfluence.getStat().getId())
                .triggerId(statInfluence.getTrigger().getId())
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
        Trigger trigger = triggerRepository.findById(influenceDTOReceive.getTriggerId()).orElseThrow(() -> new TriggerNotFoundException(influenceDTOReceive.getTriggerId()));

        Stat stat = statRepository.findById(influenceDTOReceive.getStatId()).orElseThrow(() -> new StatNotFoundException(influenceDTOReceive.getStatId()));

        if (!trigger.getVtuber().getId().equals(stat.getVtuber().getId()))
            throw new NotMatchingVtuberIdException(
                    trigger.getVtuber().getId(),
                    stat.getVtuber().getId()
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
        Trigger trigger = triggerRepository.findByVtuberIdAndLabel(vtuberId, triggerLabel).orElseThrow(() -> new TriggerNotFoundException(vtuberId, triggerLabel));
        return toDTO(statInfluenceRepository.findByTrigger(trigger));
    }

    public List<StatInfluenceDTOSend> findForStat(UUID vtuberId, String statLabel) {
        Stat stat = statRepository.findByVtuberIdAndLabel(vtuberId, statLabel).orElseThrow(() -> new StatNotFoundException(vtuberId, statLabel));
        return toDTO(statInfluenceRepository.findByStat(stat));
    }

    public StatInfluenceDTOSend update(UUID id, StatInfluenceDTOReceive influenceDTOReceive) {
        StatInfluence statInfluenceToUpdate = statInfluenceRepository.findById(id).orElseThrow(() -> new StatInfluenceNotFoundException(id));

        if (!influenceDTOReceive.getStatId().equals(statInfluenceToUpdate.getStat().getId())) {
            Stat newStat = statRepository.findById(influenceDTOReceive.getStatId()).orElseThrow(() -> new StatNotFoundException(influenceDTOReceive.getStatId()));
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

    public boolean deleteForTrigger(UUID triggerId) {
        Trigger trigger = triggerRepository.findById(triggerId).orElse(null);
        if (trigger == null) return false;

        if (!statInfluenceRepository.existsByTrigger(trigger)) return false;

        statInfluenceRepository.deleteAll(statInfluenceRepository.findByTrigger(trigger));

        return !statInfluenceRepository.existsByTrigger(trigger);
    }



    public boolean deleteForStat(UUID statId) {
        Stat stat = statRepository.findById(statId).orElse(null);
        if (stat == null) return false;

        if (!statInfluenceRepository.existsByStat(stat)) return false;
        statInfluenceRepository.deleteAll(statInfluenceRepository.findByStat(stat));
        return !statInfluenceRepository.existsByStat(stat);
    }

    public List<StatDTOSendLess> act(UUID triggerId) {
        Trigger trigger = triggerRepository.findById(triggerId).orElseThrow(() -> new TriggerNotFoundException(triggerId));

        if (trigger.getInfluences().isEmpty()) throw new TriggerCannotActException(triggerId);

        List<StatDTOSendLess> statDTOs = new ArrayList<>();
        for(StatInfluence influence : trigger.getInfluences()) {
            Stat stat = statRepository.findById(influence.getStat().getId()).orElseThrow(() -> new StatNotFoundException(influence.getStat().getId()));


            try {
                double val = Double.parseDouble(stat.getValue());
                double influenceVal = Double.parseDouble(influence.getValue());

                switch (influence.getOperator()) {
                    case "+":
                        val += influenceVal;
                        break;
                    case "-":
                        val -= influenceVal;
                        break;
                    case "*":
                        val *= influenceVal;
                        break;
                    case "/":
                        if (influenceVal != 0) {
                            val /= influenceVal;
                        }
                        break;
                    case "=":
                        val = influenceVal;
                        break;
                    default:
                        break;
                }
                stat.setValue("" + val);

            } catch (NumberFormatException e) {
                String val = stat.getValue();
                String influenceVal = influence.getValue();

                switch (influence.getOperator()) {
                    case "+":
                        val += influenceVal;
                        break;
                    case "-":
                        try {
                            int nb = Integer.parseInt(influenceVal);

                            if (nb < 0) nb = 0;

                            if (nb > val.length() - 1) nb = val.length() - 1;

                            val = val.substring(0, val.length() - 1 - nb);
                        } catch (NumberFormatException e1) {
                            val = val.replaceAll(influenceVal, "");
                        }
                        break;
                    case "*":
                        val = val.toUpperCase();
                        break;
                    case "/":
                        val = val.toLowerCase();
                        break;
                    case "=":
                        val = influenceVal;
                        break;
                    default:
                        break;
                }
                stat.setValue(val);
            }
            Stat statUpdated = statRepository.save(stat);
            statDTOs.add(StatDTOSendLess.builder()
                    .value(statUpdated.getValue())
                    .label(statUpdated.getLabel())
                    .influences(toDTO(statUpdated.getInfluences()))
                    .build()
            );
        }
        return statDTOs;
    }

}
