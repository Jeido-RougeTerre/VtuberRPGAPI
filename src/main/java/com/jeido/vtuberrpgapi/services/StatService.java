package com.jeido.vtuberrpgapi.services;

import com.jeido.vtuberrpgapi.dto.StatDTOReceive;
import com.jeido.vtuberrpgapi.dto.StatDTOSend;
import com.jeido.vtuberrpgapi.entites.Stat;
import com.jeido.vtuberrpgapi.repositories.StatRepository;
import com.jeido.vtuberrpgapi.repositories.VtuberRepository;
import com.jeido.vtuberrpgapi.utils.exceptions.stat.CanNotChangeVtuberIDException;
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

    public StatDTOSend toDTOSend(Stat stat) {
        return StatDTOSend.builder()
                .vtuberId(stat.getVtuber().getId())
                .label(stat.getLabel())
                .value(stat.getValue())
                .build();
    }

    public List<StatDTOSend> toDTOSend(List<Stat> stats) {
        List<StatDTOSend> dtoSends = new ArrayList<>();
        if (stats == null) return dtoSends;

        for (Stat stat : stats) {
            dtoSends.add(toDTOSend(stat));
        }
        return dtoSends;
    }

    public StatDTOSend create(StatDTOReceive statDTOReceive) {
        Stat statToSave = Stat.builder()
                .vtuber(vtuberRepository.findById(statDTOReceive.getVtuberId()).orElseThrow(() -> new VtuberIdNotFoundException(statDTOReceive.getVtuberId())))
                .label(statDTOReceive.getLabel())
                .value(statDTOReceive.getValue())
                .build();
        if (statRepository.existsByLabelAndVtuberId(statDTOReceive.getLabel(), statDTOReceive.getVtuberId())) {
            throw new StatLabelForVtuberIdAlreadyInDBException(statDTOReceive.getLabel(), statDTOReceive.getVtuberId());
        }


        return toDTOSend(statRepository.save(statToSave));
    }

    public StatDTOSend create(UUID vtuberId, String label) {
        if (vtuberRepository.findById(vtuberId).isEmpty()) throw new VtuberIdNotFoundException(vtuberId);
        if (statRepository.existsByLabelAndVtuberId(label, vtuberId)) throw new StatLabelForVtuberIdAlreadyInDBException(label, vtuberId);
        return toDTOSend(statRepository.save(Stat.builder()
                .vtuber(vtuberRepository.findById(vtuberId).get())
                .label(label)
                .value("")
                .build()
        ));
    }

    public List<StatDTOSend> findAll() {
        return toDTOSend((List<Stat>) statRepository.findAll());
    }

    public List<StatDTOSend> findByVtuberId(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return toDTOSend(statRepository.findByVtuberId(vtuberId));
    }

    public StatDTOSend findByVtuberIdAndLabel(UUID vtuberId, String label) {
        if (!vtuberRepository.existsById(vtuberId)) throw new VtuberIdNotFoundException(vtuberId);
        return toDTOSend(statRepository.findByLabelAndVtuberId(label, vtuberId).orElseThrow(() -> new StatLabelForVtuberIdAlreadyInDBException(label, vtuberId)));
    }


    public StatDTOSend update(String label, UUID vtuberId, StatDTOReceive statDTOReceive) {
        Stat statToUpdate = statRepository.findByLabelAndVtuberId(label, vtuberId).orElseThrow(() -> new StatNotFoundException(label, vtuberId));
        if (statDTOReceive.getVtuberId() != null && !statDTOReceive.getVtuberId().equals(statToUpdate.getVtuber().getId())) {
            throw new CanNotChangeVtuberIDException(statDTOReceive.getVtuberId(), vtuberId);
        }

        if (statDTOReceive.getLabel() != null && !statDTOReceive.getLabel().equals(statToUpdate.getLabel())) {
            if (statRepository.findByLabelAndVtuberId(statDTOReceive.getLabel(), statDTOReceive.getVtuberId()).isPresent()) {
                throw new StatLabelForVtuberIdAlreadyInDBException(statDTOReceive.getLabel(), statDTOReceive.getVtuberId());
            }
            statToUpdate.setLabel(statDTOReceive.getLabel());
        }

        if (statDTOReceive.getValue() != null && !statDTOReceive.getValue().equals(statToUpdate.getValue())) {
            statToUpdate.setValue(statDTOReceive.getValue());
        }
        return toDTOSend(statRepository.save(statToUpdate));
    }

    public boolean delete(String label, UUID vtuberId) {
        if (!statRepository.existsByLabelAndVtuberId(label, vtuberId)) return false;
        statRepository.deleteByLabelAndVtuberId(label, vtuberId);
        return !statRepository.existsByLabelAndVtuberId(label, vtuberId);
    }

    public boolean delete(UUID vtuberId) {
        if (!vtuberRepository.existsById(vtuberId)) return false;
        statRepository.deleteByVtuberId(vtuberId);
        return statRepository.findByVtuberId(vtuberId).isEmpty();
    }
}
