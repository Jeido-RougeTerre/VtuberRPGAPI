package com.jeido.vtuberrpgapi.dto.trigger;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TriggerDTOSendLess {
    private String label;
    private List<StatInfluenceDTOSend> influences;
}
