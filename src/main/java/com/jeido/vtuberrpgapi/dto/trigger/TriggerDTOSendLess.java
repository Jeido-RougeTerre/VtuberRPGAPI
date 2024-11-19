package com.jeido.vtuberrpgapi.dto.trigger;

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
public class TriggerDTOSendLess {
    private String label;
    private List<UUID> influences;
}
