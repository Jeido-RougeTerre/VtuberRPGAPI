package com.jeido.vtuberrpgapi.dto.stat;

import com.jeido.vtuberrpgapi.dto.stat_influence.StatInfluenceDTOSend;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatDTOSendFull {
    private UUID id;
    private UUID vtuberId;
    private String label;
    private String value;
    private List<StatInfluenceDTOSend> influences;
}
