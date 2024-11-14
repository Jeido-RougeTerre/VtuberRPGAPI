package com.jeido.vtuberrpgapi.dto.stat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatDTOSendFull {
    private UUID vtuberId;
    private String label;
    private String value;
}
