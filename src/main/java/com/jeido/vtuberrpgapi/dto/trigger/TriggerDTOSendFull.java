package com.jeido.vtuberrpgapi.dto.trigger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TriggerDTOSendFull {
    private String label;
    private UUID vtuberId;
}
