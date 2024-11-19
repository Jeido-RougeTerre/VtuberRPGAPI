package com.jeido.vtuberrpgapi.dto.trigger;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import com.jeido.vtuberrpgapi.entites.StatInfluence;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TriggerDTOSendFull {
    private String label;
    private UUID vtuberId;
    private List<StatInfluenceDTOSend> influences;
}
