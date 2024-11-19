package com.jeido.vtuberrpgapi.dto.stat;

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
public class StatDTOSendLess {
    private String label;
    private String value;
    private List<UUID> influences;
}
