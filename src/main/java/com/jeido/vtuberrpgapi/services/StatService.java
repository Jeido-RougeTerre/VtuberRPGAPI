package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.StatDTOReceiveFull;
import com.jeido.vtuberrpgapi.dto.StatDTOReceiveLess;
import com.jeido.vtuberrpgapi.dto.StatDTOSendFull;
import com.jeido.vtuberrpgapi.dto.StatDTOSendLess;
import com.jeido.vtuberrpgapi.entites.Stat;
import com.jeido.vtuberrpgapi.entites.keys.StatCompositeKey;
import com.jeido.vtuberrpgapi.repositories.StatRepository;
import com.jeido.vtuberrpgapi.repositories.VtuberRepository;
import com.jeido.vtuberrpgapi.utils.exceptions.stat.StatNotFoundException;
import com.jeido.vtuberrpgapi.utils.exceptions.stat.StatLabelForVtuberIdAlreadyInDBException;
import com.jeido.vtuberrpgapi.utils.exceptions.vtuber.VtuberIdNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StatService {

    private final VtuberRepository vtuberRepository;
    private final StatRepository statRepository;

    public StatService(VtuberRepository vtuberRepository, StatRepository statRepository) {
        this.vtuberRepository = vtuberRepository;
        this.statRepository = statRepository;
    }

    public StatDTOSendLess toDTOSendLess(Stat stat) {
        return StatDTOSendLess.builder()
                .label(stat.getId().getLabel())
                .value(stat.getValue())
                .build();
    }

    public List<StatDTOSendLess> toDTOSendLess(List<Stat> stats) {
        List<StatDTOSendLess> statDTOSendLessList = new ArrayList<>();
        for (Stat stat : stats) {
            statDTOSendLessList.add(toDTOSendLess(stat));
        }
        return statDTOSendLessList;
    }

    public StatDTOSendFull toDTOSendFull(Stat stat) {
        return StatDTOSendFull.builder()
                .vtuberId(stat.getId().getVtuber().getId())
                .label(stat.getId().getLabel())
                .value(stat.getValue())
                .build();
    }

    public List<StatDTOSendFull> toDTOSendFull(List<Stat> stats) {
        List<StatDTOSendFull> dtoSends = new ArrayList<>();
        if (stats == null) return dtoSends;

        for (Stat stat : stats) {
            dtoSends.add(toDTOSendFull(stat));
        }
        return dtoSends;
    }

    public StatDTOSendFull create(StatDTOReceiveFull statDTOReceiveFull) {
        Stat statToSave = Stat.builder()
                .id(new StatCompositeKey(vtuberRepository.findById(statDTOReceiveFull.getVtuberId()).orElseThrow(() -> new VtuberIdNotFoundException(statDTOReceiveFull.getVtuberId())),
                statDTOReceiveFull.getLabel()))
                .value(statDTOReceiveFull.getValue())
                .build();
        if (statRepository.existsById(new StatCompositeKey(vtuberRepository.findById(statDTOReceiveFull.getVtuberId()).get(), statDTOReceiveFull.getLabel()))) {
            throw new StatLabelForVtuberIdAlreadyInDBException(statDTOReceiveFull.getLabel(), statDTOReceiveFull.getVtuberId());
        }


        return toDTOSendFull(statRepository.save(statToSave));
    }

    public StatDTOSendFull create(UUID vtuberId, String label) {
        if (vtuberRepository.findById(vtuberId).isEmpty()) throw new VtuberIdNotFoundException(vtuberId);
        if (statRepository.existsById(new StatCompositeKey(vtuberRepository.findById(vtuberId).get(), label))) throw new StatLabelForVtuberIdAlreadyInDBException(label, vtuberId);
        return toDTOSendFull(statRepository.save(Stat.builder()
                        .id(new StatCompositeKey(vtuberRepository.findById(vtuberId).get(), label))
                .value("")
                .build()
        ));
    }

    public List<StatDTOSendFull> findAll() {
        return toDTOSendFull((List<Stat>) statRepository.findAll());
    }

    public List<StatDTOSendLess> findByVtuberId(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return toDTOSendLess(statRepository.findByVtuberId(vtuberId));
    }

    public String findByVtuberIdAndLabel(UUID vtuberId, String label) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return statRepository.findById(new StatCompositeKey(vtuberRepository.findById(vtuberId).get(), label)).orElseThrow(() -> new StatLabelForVtuberIdAlreadyInDBException(label, vtuberId)).getValue();
    }


    public StatDTOSendFull update(String label, UUID vtuberId, StatDTOReceiveLess statDTOReceiveLess) {
        StatCompositeKey key = new StatCompositeKey(vtuberRepository.findById(vtuberId)
                .orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), label);
        Stat statToUpdate = statRepository.findById(key).orElseThrow(() -> new StatNotFoundException(label, vtuberId));

        if (statDTOReceiveLess.getLabel() != null && !statDTOReceiveLess.getLabel().equals(statToUpdate.getId().getLabel())) {
            StatCompositeKey newKey = new StatCompositeKey(vtuberRepository.findById(vtuberId).get(), statDTOReceiveLess.getLabel());
            if (statRepository.findById(newKey).isPresent()) {
                throw new StatLabelForVtuberIdAlreadyInDBException(statDTOReceiveLess.getLabel(), vtuberId);
            }
            statToUpdate.setId(newKey);
            statToUpdate.setValue(statToUpdate.getValue());
            statRepository.deleteById(key);
        }

        if (statDTOReceiveLess.getValue() != null && !statDTOReceiveLess.getValue().equals(statToUpdate.getValue())) {
            statToUpdate.setValue(statDTOReceiveLess.getValue());
        }
        return toDTOSendFull(statRepository.save(statToUpdate));
    }

    public boolean delete(String label, UUID vtuberId) {
        StatCompositeKey key = new StatCompositeKey(vtuberRepository.findById(vtuberId)
                .orElseThrow(() -> new VtuberIdNotFoundException(vtuberId)), label);
        if (!statRepository.existsById(key)) return false;
        statRepository.deleteById(key);
        return !statRepository.existsById(key);
    }

    public boolean delete(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) return false;
        statRepository.deleteAll(statRepository.findByVtuberId(vtuberId));
        return statRepository.findByVtuberId(vtuberId).isEmpty();
    }
}
