package com.jeido.vtuberrpgapi.dto.stat_influence;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "Trigger ID should not be Empty")
    private UUID triggerId;

    @NotNull(message = "Stat ID should not be Empty")
    private UUID statId;

    private String value;

    @Pattern(regexp = "^[+*/=-]$", message = "Valid operators are +, -, *, /, =")
    private String operator;

}
