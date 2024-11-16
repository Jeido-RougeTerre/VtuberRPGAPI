package com.jeido.vtuberrpgapi.dto.stat_influence;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatInfluenceDTOReceive {
    @NotNull
    private UUID vtuberId;
    @NotNull
    private String triggerLabel;
    @NotNull
    private String statLabel;
    private String value;
    @Pattern(regexp = "^[+*/=-]$")
    private String operator;

}
