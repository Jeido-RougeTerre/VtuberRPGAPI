package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.stat.StatDTOReceiveFull;
import com.jeido.vtuberrpgapi.dto.stat.StatDTOReceiveLess;
import com.jeido.vtuberrpgapi.dto.stat.StatDTOSendFull;
import com.jeido.vtuberrpgapi.dto.stat.StatDTOSendLess;
import com.jeido.vtuberrpgapi.entites.Stat;
import com.jeido.vtuberrpgapi.entites.Vtuber;
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
    private final StatInfluenceService statInfluenceService;

    public StatService(VtuberRepository vtuberRepository, StatRepository statRepository, StatInfluenceService statInfluenceService) {
        this.vtuberRepository = vtuberRepository;
        this.statRepository = statRepository;
        this.statInfluenceService = statInfluenceService;
    }

    public StatDTOSendLess toDTOSendLess(Stat stat) {
        return StatDTOSendLess.builder()
                .label(stat.getLabel())
                .value(stat.getValue())
                .influences(statInfluenceService.toDTO(stat.getInfluences()))
                .build();
    }

    public List<StatDTOSendLess> toDTOSendLess(List<Stat> stats) {
        List<StatDTOSendLess> statDTOSendLessList = new ArrayList<>();
        if (stats != null) {
            for (Stat stat : stats) {
                statDTOSendLessList.add(toDTOSendLess(stat));
            }
        }
        return statDTOSendLessList;
    }

    public StatDTOSendFull toDTOSendFull(Stat stat) {
        return StatDTOSendFull.builder()
                .id(stat.getId())
                .vtuberId(stat.getVtuber().getId())
                .label(stat.getLabel())
                .value(stat.getValue())
                .influences(statInfluenceService.toDTO(stat.getInfluences()))
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
        Vtuber vtuber = vtuberRepository.findById(statDTOReceiveFull.getVtuberId()).orElseThrow(() -> new VtuberIdNotFoundException(statDTOReceiveFull.getVtuberId()));
        if (statRepository.existsByVtuberIdAndLabel(statDTOReceiveFull.getVtuberId(), statDTOReceiveFull.getLabel())) {
            throw new StatLabelForVtuberIdAlreadyInDBException(statDTOReceiveFull.getLabel(), statDTOReceiveFull.getVtuberId());
        }


        return toDTOSendFull(statRepository.save(Stat.builder()
                        .vtuber(vtuber)
                .label(statDTOReceiveFull.getLabel())
                .value(statDTOReceiveFull.getValue())
                .build()
        ));
    }

    public StatDTOSendFull create(UUID vtuberId, String label) {
        Vtuber vtuber = vtuberRepository.findById(vtuberId).orElseThrow(() -> new VtuberIdNotFoundException(vtuberId));
        if (statRepository.existsByVtuberIdAndLabel(vtuberId, label)) throw new StatLabelForVtuberIdAlreadyInDBException(label, vtuberId);
        return toDTOSendFull(statRepository.save(Stat.builder()
                        .vtuber(vtuber)
                .label(label)
                .value("")
                .build()
        ));
    }

    public List<StatDTOSendFull> findAll() {
        return toDTOSendFull((List<Stat>) statRepository.findAll());
    }

    public StatDTOSendFull findById(UUID id) {
        return toDTOSendFull(statRepository.findById(id).orElseThrow(() -> new StatNotFoundException(id)));
    }

    public List<StatDTOSendFull> findByVtuberId(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return toDTOSendFull(statRepository.findByVtuberId(vtuberId));
    }

    public StatDTOSendFull findByVtuberIdAndLabel(UUID vtuberId, String label) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return toDTOSendFull(statRepository.findByVtuberIdAndLabel(vtuberId, label).orElseThrow(() -> new StatNotFoundException(vtuberId, label)));
    }


    public StatDTOSendFull update(UUID id, StatDTOReceiveLess statDTOReceiveLess) {
        Stat statToUpdate = statRepository.findById(id).orElseThrow(() -> new StatNotFoundException(id));

        if (statDTOReceiveLess.getLabel() != null && !statDTOReceiveLess.getLabel().equals(statToUpdate.getLabel())) {
            if (statRepository.existsByVtuberIdAndLabel(statToUpdate.getVtuber().getId(), statDTOReceiveLess.getLabel())) {
                throw new StatLabelForVtuberIdAlreadyInDBException(statDTOReceiveLess.getLabel(), statToUpdate.getVtuber().getId());
            }
            statToUpdate.setLabel(statDTOReceiveLess.getLabel());
        }

        if (statDTOReceiveLess.getValue() != null && !statDTOReceiveLess.getValue().equals(statToUpdate.getValue())) {
            statToUpdate.setValue(statDTOReceiveLess.getValue());
        }
        return toDTOSendFull(statRepository.save(statToUpdate));
    }

    public boolean delete(UUID id) {
        if (!statRepository.existsById(id)) return false;
        statInfluenceService.deleteForStat(id);
        statRepository.deleteById(id);
        return !statRepository.existsById(id);
    }

    public boolean deleteByVtuber(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) return false;

        statRepository.deleteAll(statRepository.findByVtuberId(vtuberId));

        return statRepository.findByVtuberId(vtuberId).isEmpty();
    }
}
