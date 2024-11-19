package com.jeido.vtuberrpgapi.dto.stat_influence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatInfluenceDTOSend {
    private UUID id;
    private UUID vtuberId;
    private String triggerLabel;
    private String statLabel;
    private String value;
    private String operator;
}
