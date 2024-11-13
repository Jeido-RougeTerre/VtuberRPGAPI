package com.jeido.vtuberrpgapi.dto;

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
public class VtuberDTOSend {
    private UUID id;
    private String name;
    private List<UUID> userIds;
    private List<StatDTOSendLess> stats;
}